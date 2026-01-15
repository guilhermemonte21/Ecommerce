package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Pagamento;

import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.PedidoNotFoundException;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.PedidoDoVendedorGateway;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.PedidoGateway;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.UsuarioGateway;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Pedidos.ChangePedidoStatus.ChangePedidoStatus;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.PedidoDoVendedor;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Pedidos;
import com.github.guilhermemonte21.Ecommerce.Domain.Enum.StatusPedido;

import java.util.List;
import java.util.UUID;

public class Pagamento {

    private final PedidoGateway pedidoGateway;
    private final ChangePedidoStatus change;
    private final PedidoDoVendedorGateway pedidoDoVendedorGateway;

    public Pagamento(PedidoGateway pedidoGateway, ChangePedidoStatus change, PedidoDoVendedorGateway pedidoDoVendedorGateway) {
        this.pedidoGateway = pedidoGateway;
        this.change = change;
        this.pedidoDoVendedorGateway = pedidoDoVendedorGateway;
    }

    public Boolean pagar(UUID IdPedido){
        Pedidos order = pedidoGateway.getById(IdPedido).orElseThrow(() -> new PedidoNotFoundException(IdPedido));

        List<PedidoDoVendedor> itens = order.getItens();
        for (PedidoDoVendedor seller : itens){
            seller.setStatus("Pago");

        }
        change.ChangePedidosStatus(order.getId());


        pedidoGateway.save(order);
        return true;
    }
}
