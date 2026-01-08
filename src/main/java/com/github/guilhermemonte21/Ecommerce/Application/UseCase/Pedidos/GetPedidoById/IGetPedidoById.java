package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Pedidos.GetPedidoById;

import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Pedidos;

import java.util.Optional;
import java.util.UUID;

public interface IGetPedidoById {
    Optional<Pedidos> pedidoById(UUID IdPedido);
}
