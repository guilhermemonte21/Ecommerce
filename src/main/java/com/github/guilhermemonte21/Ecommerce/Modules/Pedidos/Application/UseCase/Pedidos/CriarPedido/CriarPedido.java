package com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.UseCase.Pedidos.CriarPedido;

import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.DTO.Pedidos.PedidoResponse;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions.*;
import com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Application.Gateway.CarrinhoGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.Gateway.PedidoGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.Gateway.ProdutoGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.Gateway.UsuarioAutenticadoGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.Gateway.UsuarioGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.Mappers.PedidoMapperApl;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Port.EventPublisher;
import com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Domain.Entity.Carrinho;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Domain.Entity.Produtos;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Domain.Entity.Pedidos;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Domain.Entity.PedidoDoVendedor;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Domain.Entity.UsuarioAutenticado;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Domain.Enum.StatusPedido;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Domain.Event.PedidoCriadoEvent;
import com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Domain.Entity.CarrinhoItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.*;

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
    public PedidoResponse criarPedido(String endereco) {
        if (endereco == null || endereco.isBlank()) {
            throw new IllegalArgumentException("Endereço de entrega é obrigatório");
        }
        UsuarioAutenticado user = authGateway.get();
        if (user.getUser().getId() == null) {
            throw new IllegalArgumentException("Usuário não autenticado");
        }
        Carrinho cart = carrinhoGateway.getByDono(user.getUser().getId());
        if (cart == null || cart.getItens() == null || cart.getItens().isEmpty()) {
            throw new CarrinhoVazioException();
        }

        Map<UUID, Long> productQuantities = new HashMap<>();
        for (CarrinhoItem itemCarrinho : cart.getItens()) {
            productQuantities.put(itemCarrinho.getProdutoId(),
                    productQuantities.getOrDefault(itemCarrinho.getProdutoId(), 0L) + itemCarrinho.getQuantidade());
        }
        Map<UUID, Produtos> productDetails = new HashMap<>();
        for (Map.Entry<UUID, Long> entry : productQuantities.entrySet()) {
            UUID idProduto = entry.getKey();
            Long quantidade = entry.getValue();
            Produtos produtoComLock = produtoGateway.getByIdComLock(idProduto)
                    .orElseThrow(() -> new ProdutoNotFoundException(idProduto));

            if (produtoComLock.getVendedorId().equals(user.getUser().getId())) {
                throw new IllegalArgumentException(
                        "Não é possível comprar o próprio produto: " + produtoComLock.getNomeProduto());
            }

            if (produtoComLock.getEstoque() == null || produtoComLock.getEstoque() < quantidade) {
                throw new EstoqueInsuficienteException(produtoComLock.getNomeProduto());
            }

            produtoComLock.setEstoque(produtoComLock.getEstoque() - quantidade);
            produtoGateway.salvar(produtoComLock);
            productDetails.put(idProduto, produtoComLock);
        }
        Pedidos pedido = new Pedidos();
        pedido.setCompradorId(user.getUser().getId());

        List<PedidoDoVendedor> orderItems = new ArrayList<>();
        for (CarrinhoItem item : cart.getItens()) {
            Produtos dbProduct = productDetails.get(item.getProdutoId());
            UUID vendedorId = dbProduct.getVendedorId();
            
            PedidoDoVendedor pedidoItem = new PedidoDoVendedor();
            pedidoItem.setVendedorId(vendedorId);
            pedidoItem.setPedido(pedido);
            pedidoItem.setProdutoId(item.getProdutoId());
            pedidoItem.setNomeProduto(dbProduct.getNomeProduto());
            pedidoItem.setPrecoUnitario(dbProduct.getPreco());
            pedidoItem.setQuantidade(item.getQuantidade());
            pedidoItem.setValor(dbProduct.getPreco().multiply(BigDecimal.valueOf(item.getQuantidade())));
            pedidoItem.setStatus(StatusPedido.PENDENTE);

            usuarioGateway.getById(vendedorId).ifPresent(vendedor -> pedidoItem.setStripeAccountId(vendedor.getStripeAccountId()));

            orderItems.add(pedidoItem);
        }

        pedido.setItens(orderItems);
        pedido.setPreco(orderItems.stream()
                .map(PedidoDoVendedor::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        pedido.setEndereco(endereco);
        pedido.setCriadoEm(OffsetDateTime.now());

        Pedidos completeOrder = pedidoGateway.save(pedido);

        eventPublisher.publish(new PedidoCriadoEvent(completeOrder.getId(), user.getUser().getId()));
        log.info("Pedido criado com sucesso: id={}, comprador={}", completeOrder.getId(), user.getUser().getId());
        return mapperApl.toResponse(completeOrder);
    }
}
