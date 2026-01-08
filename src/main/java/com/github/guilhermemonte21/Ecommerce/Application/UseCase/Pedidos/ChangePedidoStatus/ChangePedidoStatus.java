package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Pedidos.ChangePedidoStatus;

import com.github.guilhermemonte21.Ecommerce.Application.Gateway.PedidoGateway;
import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Pedidos;
import com.github.guilhermemonte21.Ecommerce.Domain.Model.Enum.StatusPedido;

import java.util.Optional;
import java.util.UUID;

public class ChangePedidoStatus implements IChangePedidoStatus{
    private final PedidoGateway gateway;

    public ChangePedidoStatus(PedidoGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public void ChangePedidosStatus(UUID IdPedido, StatusPedido status) {
        Optional<Pedidos> pedidos = gateway.getById(IdPedido);

        pedidos.get().setStatus(status);

        gateway.save(pedidos.get());
    }
}
