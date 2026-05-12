package com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.UseCase.Pedidos.CriarPedido;

import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.DTO.Pedidos.CriarPedidoRequest;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.DTO.Pedidos.PedidoResponse;
import com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Application.Gateway.CarrinhoGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.Gateway.PedidoGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.Gateway.ProdutoGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.Gateway.UsuarioAutenticadoGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.Gateway.UsuarioGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.Mappers.PedidoMapperApl;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions.ProdutoNaoPertenceAoVendedorException;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions.ProdutoNotFoundException;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions.UsuarioNotFoundException;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Port.EventPublisher;
import com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Domain.Entity.Carrinho;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Domain.Entity.Produtos;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Domain.Entity.Pedidos;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Domain.Entity.UsuarioAutenticado;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Domain.Entity.Usuarios;
import com.github.guilhermemonte21.Ecommerce.Shared.Domain.Event.PedidoCriadoEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

public class CriarPedido implements ICriarPedido {

    private static final Logger log = LoggerFactory.getLogger(CriarPedido.class);

    private final PedidoGateway pedidoGateway;
    private final CarrinhoGateway carrinhoGateway;
    private final ProdutoGateway produtoGateway;
    private final PedidoMapperApl mapperApl;
    private final UsuarioAutenticadoGateway authGateway;
    private final EventPublisher eventPublisher;
    private final UsuarioGateway usuarioGateway;

    public CriarPedido(PedidoGateway pedidoGateway, CarrinhoGateway carrinhoGateway, ProdutoGateway produtoGateway,
            PedidoMapperApl mapperApl, UsuarioAutenticadoGateway authGateway, EventPublisher eventPublisher,
            UsuarioGateway usuarioGateway) {
        this.pedidoGateway = pedidoGateway;
        this.carrinhoGateway = carrinhoGateway;
        this.produtoGateway = produtoGateway;
        this.mapperApl = mapperApl;
        this.authGateway = authGateway;
        this.eventPublisher = eventPublisher;
        this.usuarioGateway = usuarioGateway;
    }

    @Transactional
    @Override
    public PedidoResponse criarPedido(CriarPedidoRequest request) {
        String endereco = request.endereco();
        UsuarioAutenticado user = authGateway.get();
        UUID compradorId = user.getUser().getId();

        Carrinho cart = carrinhoGateway.getByDono(compradorId);

        cart.validarNaoVazia();

        Map<UUID, Produtos> produtosReservados = reservarEstoques(cart.agregarQuantidadesPorProduto(), compradorId);

        produtoGateway.saveAll(new ArrayList<>(produtosReservados.values()));

        Map<UUID, String> stripeAccounts = buscarContasStripe(produtosReservados);

        Pedidos pedido = Pedidos.criarDe(compradorId, cart.getItens(), produtosReservados, stripeAccounts, endereco);
        Pedidos pedidoSalvo = pedidoGateway.save(pedido);

        Usuarios comprador = usuarioGateway.getById(compradorId)
                .orElseThrow(() -> new UsuarioNotFoundException(compradorId));

        eventPublisher.publish(new PedidoCriadoEvent(
                pedidoSalvo.getId(),
                comprador.getId(),
                comprador.getNome(),
                comprador.getEmail(),
                pedidoSalvo.getPreco()));

        log.info("Pedido criado com sucesso: id={}, comprador={}", pedidoSalvo.getId(), compradorId);
        return mapperApl.toResponse(pedidoSalvo);
    }

    private Map<UUID, Produtos> reservarEstoques(Map<UUID, Long> quantidades, UUID compradorId) {
        Map<UUID, Produtos> produtosReservados = new HashMap<>();
        for (Map.Entry<UUID, Long> entry : quantidades.entrySet()) {
            UUID idProduto = entry.getKey();
            Long quantidade = entry.getValue();

            Produtos produto = produtoGateway.getByIdComLock(idProduto)
                    .orElseThrow(() -> new ProdutoNotFoundException(idProduto));

            if (produto.pertenceA(compradorId)) {
                throw new ProdutoNaoPertenceAoVendedorException(produto.getNomeProduto());
            }

            produto.reservarEstoque(quantidade);
            produtosReservados.put(idProduto, produto);
        }
        return produtosReservados;
    }

    private Map<UUID, String> buscarContasStripe(Map<UUID, Produtos> produtos) {
        List<UUID> vendedorIds = produtos.values().stream()
                .map(Produtos::getVendedorId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        return usuarioGateway.findAllByIds(vendedorIds).stream()
                .filter(u -> u.getStripeAccountId() != null)
                .collect(Collectors.toMap(Usuarios::getId, Usuarios::getStripeAccountId));
    }
}
