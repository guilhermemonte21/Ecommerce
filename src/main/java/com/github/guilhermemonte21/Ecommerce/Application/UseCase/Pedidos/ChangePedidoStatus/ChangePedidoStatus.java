package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Pedidos.ChangePedidoStatus;

import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.PedidoNotFoundException;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.PedidoGateway;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Pedidos;
import com.github.guilhermemonte21.Ecommerce.Domain.Enum.StatusPedido;

import java.util.UUID;

public class ChangePedidoStatus implements IChangePedidoStatus{
    private final PedidoGateway gateway;

    public ChangePedidoStatus(PedidoGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public void ChangePedidosStatus(UUID IdPedido, StatusPedido status) {
        Pedidos pedidos = gateway.getById(IdPedido).orElseThrow(() -> new PedidoNotFoundException(IdPedido));

        pedidos.setStatus(status);

        gateway.save(pedidos);
    }
}
