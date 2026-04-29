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
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Domain.Entity.Usuarios;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Domain.Enum.StatusPedido;
import com.github.guilhermemonte21.Ecommerce.Shared.Domain.Event.PedidoCriadoEvent;
import com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Domain.Entity.CarrinhoItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
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
            productDetails.put(idProduto, produtoComLock);
        }
        List<Produtos> updatedProducts = new ArrayList<>(productDetails.values());
        produtoGateway.saveAll(updatedProducts);
        Pedidos pedido = new Pedidos();
        pedido.setCompradorId(user.getUser().getId());

        List<UUID> allVendedorIds = cart.getItens().stream()
                .map(item -> productDetails.get(item.getProdutoId()).getVendedorId())
                .distinct()
                .toList();

        Map<UUID, String> sellerStripeAccounts = usuarioGateway.findAllByIds(allVendedorIds).stream()
                .filter(u -> u.getStripeAccountId() != null)
                .collect(Collectors.toMap(Usuarios::getId, Usuarios::getStripeAccountId));

        List<PedidoDoVendedor> orderItems = new ArrayList<>();
        for (CarrinhoItem item : cart.getItens()) {
            Produtos dbProduct = productDetails.get(item.getProdutoId());
            UUID sellerId = dbProduct.getVendedorId();

            PedidoDoVendedor pedidoItem = PedidoDoVendedor.builder()
                    .vendedorId(sellerId)
                    .pedido(pedido)
                    .produtoId(item.getProdutoId())
                    .nomeProduto(dbProduct.getNomeProduto())
                    .precoUnitario(dbProduct.getPreco())
                    .quantidade(item.getQuantidade())
                    .valor(dbProduct.getPreco().multiply(BigDecimal.valueOf(item.getQuantidade())))
                    .status(StatusPedido.PENDENTE)

                    .stripeAccountId(sellerStripeAccounts.get(sellerId))
                    .build();

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
