package com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.UseCase.Pedidos.GetPedidoById;

import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.DTO.Pedidos.PedidoResponse;

import java.util.UUID;

public interface IGetPedidoById {
    PedidoResponse pedidoById(UUID IdPedido);
}
