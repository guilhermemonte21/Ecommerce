package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Pagamento;

import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.PedidoNotFoundException;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.PedidoGateway;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.ProdutoGateway;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Pedidos;
import java.util.UUID;

public class Pagamento implements IPagamento {
    private final PedidoGateway pedidoGateway;
    private final ProdutoGateway produtoGateway;

    public Pagamento(PedidoGateway pedidoGateway, ProdutoGateway produtoGateway) {
        this.pedidoGateway = pedidoGateway;
        this.produtoGateway = produtoGateway;
    }

    public Boolean pagar(UUID IdPedido) {
        Pedidos order = pedidoGateway.getById(IdPedido).orElseThrow(() -> new PedidoNotFoundException(IdPedido));

        order.confirmarPagamento();

        pedidoGateway.save(order);
        return true;
    }
}
