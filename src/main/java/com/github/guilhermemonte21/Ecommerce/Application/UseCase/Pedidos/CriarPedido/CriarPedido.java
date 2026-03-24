package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Pedidos.CriarPedido;

import com.github.guilhermemonte21.Ecommerce.Application.DTO.Pedidos.PedidoResponse;
import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.CarrinhoNotFoundException;
import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.CarrinhoVazioException;
import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.EstoqueInsuficienteException;
import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.ProdutoNotFoundException;
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
    public PedidoResponse criarPedido(UUID carrinhoId, String endereco) {
        UsuarioAutenticado user = authGateway.get();
        Carrinho cart = carrinhoGateway.getById(carrinhoId)
                .orElseThrow(() -> new CarrinhoNotFoundException(carrinhoId));

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
        }

        Pedidos pedido = new Pedidos();
        pedido.setComprador(user.getUser());
        Pedidos salvo = pedidoGateway.save(pedido);

        Map<UUID, PedidoDoVendedor> orders = new HashMap<>();
        for (Produtos produto : cart.getItens()) {
            UUID vendedorId = produto.getVendedor().getId();
            PedidoDoVendedor pedidoVendedor = orders.computeIfAbsent(vendedorId, id -> {
                PedidoDoVendedor novo = new PedidoDoVendedor();
                novo.setVendedor(produto.getVendedor());
                novo.setPedido(salvo.getId());
                novo.setValor(BigDecimal.ZERO);
                novo.setStatus(StatusPedido.CRIADO);
                return novo;
            });
            pedidoVendedor.getProdutos().add(produto);
            pedidoVendedor.setValor(pedidoVendedor.getValor().add(produto.getPreco()));
        }

        salvo.getItens().addAll(orders.values());
        salvo.setPreco(orders.values().stream()
                .map(PedidoDoVendedor::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        salvo.setEndereço(endereco);
        salvo.setCriadoEm(OffsetDateTime.now());

        Pedidos completeOrder = pedidoGateway.save(salvo);
        carrinhoGateway.limparCarrinho(cart);

        log.info("Pedido criado com sucesso: id={}, comprador={}", completeOrder.getId(), user.getUser().getId());
        return mapperApl.toResponse(completeOrder);
    }
}