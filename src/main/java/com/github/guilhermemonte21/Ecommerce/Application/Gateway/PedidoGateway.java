package com.github.guilhermemonte21.Ecommerce.Application.Gateway;

import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Pedidos;

import java.util.Optional;
import java.util.UUID;

public interface PedidoGateway {

    Pedidos save(Pedidos pedidosEntity);

    Optional<Pedidos> getById(UUID Id);
}
