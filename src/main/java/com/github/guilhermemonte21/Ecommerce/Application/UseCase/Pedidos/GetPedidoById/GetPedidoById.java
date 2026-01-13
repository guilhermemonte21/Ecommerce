package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Pedidos.GetPedidoById;

import com.github.guilhermemonte21.Ecommerce.Application.DTO.Pedidos.PedidoResponse;
import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.PedidoNotFoundException;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.PedidoGateway;
import com.github.guilhermemonte21.Ecommerce.Application.Mappers.PedidoMapperApl;
import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Pedidos;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class GetPedidoById implements IGetPedidoById{
    private final PedidoGateway gateway;
    private final PedidoMapperApl mapperApl;

    public GetPedidoById(PedidoGateway gateway, PedidoMapperApl mapperApl) {
        this.gateway = gateway;
        this.mapperApl = mapperApl;
    }

    @Override
    public PedidoResponse pedidoById(UUID IdPedido) {
        Pedidos pedidos = gateway.getById(IdPedido).orElseThrow(() -> new PedidoNotFoundException(IdPedido));
        PedidoResponse response = mapperApl.toResponse(pedidos);

        return response;
    }
}
