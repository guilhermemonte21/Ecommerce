package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Pagamento;

import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.PedidoNotFoundException;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.PedidoGateway;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.ProdutoGateway;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.PedidoDoVendedor;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Pedidos;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Produtos;

import java.util.UUID;

public class Pagamento implements IPagamento {

    private final PedidoGateway pedidoGateway;
    private final ProdutoGateway produtoGateway;

    public Pagamento(PedidoGateway pedidoGateway, ProdutoGateway produtoGateway) {
        this.pedidoGateway = pedidoGateway;
        this.produtoGateway = produtoGateway;
    }

    // TODO: substituir por integração real com Stripe
    @Override
    public Boolean pagar(UUID idPedido) {
        Pedidos pedido = pedidoGateway.getById(idPedido)
                .orElseThrow(() -> new PedidoNotFoundException(idPedido));
        pedido.confirmarPagamento();
        pedidoGateway.save(pedido);
        return true;
    }

    @Override
    public void cancelarPagamento(UUID idPedido) {
        Pedidos pedido = pedidoGateway.getById(idPedido)
                .orElseThrow(() -> new PedidoNotFoundException(idPedido));

        for (PedidoDoVendedor subPedido : pedido.getItens()) {
            for (Produtos produto : subPedido.getProdutos()) {
                produtoGateway.getById(produto.getId()).ifPresent(p -> {
                    p.atualizarEstoque(1L);
                    produtoGateway.salvar(p);
                });
            }
        }

        pedido.cancelar();
        pedidoGateway.save(pedido);
    }
}
