package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Pedidos.GetPedidoById;

import com.github.guilhermemonte21.Ecommerce.Application.DTO.Pedidos.PedidoResponse;

import java.util.UUID;

public interface IGetPedidoById {
    PedidoResponse pedidoById(UUID IdPedido);
}
