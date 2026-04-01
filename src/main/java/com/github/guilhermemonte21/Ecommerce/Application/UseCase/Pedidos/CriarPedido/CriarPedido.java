package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Pedidos.CriarPedido;

import com.github.guilhermemonte21.Ecommerce.Application.DTO.Pedidos.PedidoResponse;
import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.*;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.CarrinhoGateway;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.PedidoGateway;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.ProdutoGateway;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.UsuarioAutenticadoGateway;
import com.github.guilhermemonte21.Ecommerce.Application.Mappers.PedidoMapperApl;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.*;
import com.github.guilhermemonte21.Ecommerce.Domain.Enum.StatusPedido;
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

    public CriarPedido(PedidoGateway pedidoGateway, CarrinhoGateway carrinhoGateway, ProdutoGateway produtoGateway,
            PedidoMapperApl mapperApl, UsuarioAutenticadoGateway authGateway) {
        this.pedidoGateway = pedidoGateway;
        this.carrinhoGateway = carrinhoGateway;
        this.produtoGateway = produtoGateway;
        this.mapperApl = mapperApl;
        this.authGateway = authGateway;
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
        if (cart.getItens() == null || cart.getItens().isEmpty()) {
            throw new CarrinhoVazioException();
        }
        for (Produtos item : cart.getItens()) {
            if (item.getVendedor().getId().equals(user.getUser().getId())) {
                throw new IllegalArgumentException(
                        "Não é possível comprar o próprio produto: " + item.getNomeProduto());
            }
        }
        Map<UUID, Long> productQuantities = new HashMap<>();
        for (Produtos itemCarrinho : cart.getItens()) {
            productQuantities.put(itemCarrinho.getId(),
                    productQuantities.getOrDefault(itemCarrinho.getId(), 0L) + 1);
        }
        Map<UUID, Produtos> productDetails = new HashMap<>();
        for (Map.Entry<UUID, Long> entry : productQuantities.entrySet()) {
            UUID idProduto = entry.getKey();
            Long quantidade = entry.getValue();
            Produtos produtoComLock = produtoGateway.getByIdComLock(idProduto)
                    .orElseThrow(() -> new ProdutoNotFoundException(idProduto));

            if (produtoComLock.getEstoque() == null || produtoComLock.getEstoque() < quantidade) {
                throw new EstoqueInsuficienteException(produtoComLock.getNomeProduto());
            }

            produtoComLock.setEstoque(produtoComLock.getEstoque() - quantidade);
            produtoGateway.salvar(produtoComLock);
            productDetails.put(idProduto, produtoComLock);
        }
        Pedidos pedido = new Pedidos();
        pedido.setComprador(user.getUser());

        Map<UUID, PedidoDoVendedor> orders = new HashMap<>();
        for (Produtos itemCarrinho : cart.getItens()) {
            Produtos dbProduct = productDetails.get(itemCarrinho.getId());
            UUID vendedorId = dbProduct.getVendedor().getId();
            PedidoDoVendedor pedidoVendedor = orders.computeIfAbsent(vendedorId, id -> {
                PedidoDoVendedor novo = new PedidoDoVendedor();
                novo.setVendedor(dbProduct.getVendedor());
                novo.setPedido(pedido);
                novo.setValor(BigDecimal.ZERO);
                novo.setStatus(StatusPedido.PENDENTE);
                return novo;
            });

            pedidoVendedor.getProdutos().add(dbProduct);
            pedidoVendedor.setValor(pedidoVendedor.getValor().add(dbProduct.getPreco()));
        }

        pedido.getItens().addAll(orders.values());
        pedido.setPreco(orders.values().stream()
                .map(PedidoDoVendedor::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        pedido.setEndereço(endereco);
        pedido.setCriadoEm(OffsetDateTime.now());

        Pedidos completeOrder = pedidoGateway.save(pedido);
        cart.limpar();
        carrinhoGateway.save(cart);
        log.info("Pedido criado com sucesso: id={}, comprador={}", completeOrder.getId(), user.getUser().getId());
        return mapperApl.toResponse(completeOrder);
    }
}