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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.*;

@Service
public class CriarPedido implements ICriarPedido {

    private final PedidoGateway Pedidogateway;
    private final CarrinhoGateway CarrinhoGateway;
    private final ProdutoGateway ProdutoGateway;
    private final PedidoMapperApl mapperApl;
    private final UsuarioAutenticadoGateway AuthGateway;

    public CriarPedido(PedidoGateway pedidogateway, CarrinhoGateway carrinhoGateway, ProdutoGateway produtoGateway,
            PedidoMapperApl mapperApl, UsuarioAutenticadoGateway authGateway) {
        Pedidogateway = pedidogateway;
        CarrinhoGateway = carrinhoGateway;
        ProdutoGateway = produtoGateway;
        this.mapperApl = mapperApl;
        AuthGateway = authGateway;
    }

    @Transactional
    @Override
    public PedidoResponse CriarPedido(UUID CarrinhoId, String Endereco) {
        UsuarioAutenticado user = AuthGateway.get();
        Carrinho cart = CarrinhoGateway.getById(CarrinhoId)
                .orElseThrow(() -> new CarrinhoNotFoundException(CarrinhoId));

        if (cart.getItens() == null || cart.getItens().isEmpty()) {
            throw new CarrinhoVazioException();
        }
        for (Produtos item : cart.getItens()) {
            if (item.getVendedor().getId().equals(user.getUser().getId())) {
                throw new IllegalArgumentException(
                        "Não é possível comprar o próprio produto: " + item.getNomeProduto());
            }
        }
        for (Produtos itemCarrinho : cart.getItens()) {
            Produtos produtoComLock = ProdutoGateway.GetByIdComLock(itemCarrinho.getId())
                    .orElseThrow(() -> new ProdutoNotFoundException(itemCarrinho.getId()));

            if (produtoComLock.getEstoque() == null || produtoComLock.getEstoque() < 1) {
                throw new EstoqueInsuficienteException(produtoComLock.getNomeProduto());
            }

            produtoComLock.setEstoque(produtoComLock.getEstoque() - 1);
            ProdutoGateway.salvar(produtoComLock);
        }

        Pedidos Pedido = new Pedidos();
        Pedido.setComprador(user.getUser());
        Pedidos salvo = Pedidogateway.save(Pedido);

        Map<UUID, PedidoDoVendedor> orders = new HashMap<>();
        for (Produtos produto : cart.getItens()) {
            UUID vendedorId = produto.getVendedor().getId();
            PedidoDoVendedor pedido = orders.computeIfAbsent(vendedorId, id -> {
                PedidoDoVendedor novo = new PedidoDoVendedor();
                novo.setVendedor(produto.getVendedor());
                novo.setPedido(salvo.getId());
                novo.setValor(BigDecimal.ZERO);
                novo.setStatus(StatusPedido.CRIADO);
                return novo;
            });
            pedido.getProdutos().add(produto);
            pedido.setValor(pedido.getValor().add(produto.getPreco()));
        }
        salvo.getItens().addAll(orders.values());
        salvo.setPreco(orders.values().stream()
                .map(PedidoDoVendedor::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        salvo.setEndereço(Endereco);
        salvo.setCriadoEm(OffsetDateTime.now());

        Pedidos CompleteOrder = Pedidogateway.save(salvo);
        CarrinhoGateway.LimparCarrinho(cart);
        return mapperApl.toResponse(CompleteOrder);
    }
}