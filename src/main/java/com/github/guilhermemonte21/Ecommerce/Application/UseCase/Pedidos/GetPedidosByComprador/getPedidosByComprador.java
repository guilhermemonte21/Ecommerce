package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Pedidos.GetPedidosByComprador;

import com.github.guilhermemonte21.Ecommerce.Application.Gateway.PedidoGateway;
import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Pedidos;

import java.util.List;
import java.util.UUID;

public class getPedidosByComprador implements IGetPedidosByComprador{

    private final PedidoGateway gateway;

    public getPedidosByComprador(PedidoGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public List<Pedidos> getPedidosByComprador(UUID IdComprador) {
        List<Pedidos> pedidos = gateway.getPedidosByComprador(IdComprador);

        if (pedidos.isEmpty()){
            throw new RuntimeException("Esse Cliente não fez pedidos ainda");
        }

        return pedidos;
    }
}
