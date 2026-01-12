package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Pedidos.GetPedidoById;

import com.github.guilhermemonte21.Ecommerce.Application.Gateway.PedidoGateway;
import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Pedidos;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class GetPedidoById implements IGetPedidoById{
    private final PedidoGateway gateway;

    public GetPedidoById(PedidoGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public Optional<Pedidos> pedidoById(UUID IdPedido) {
        Optional<Pedidos> pedidos = gateway.getById(IdPedido);
        if (pedidos.isEmpty()){
            throw new RuntimeException("Pedido não encotrado");
        }

        return pedidos;
    }
}
