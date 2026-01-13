package com.github.guilhermemonte21.Ecommerce.Application.Gateway;

import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Pedidos;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PedidoGateway {

    Pedidos save(Pedidos pedidosEntity);

    Optional<Pedidos> getById(UUID Id);
    List<Pedidos> getPedidosByComprador(UUID IdComprador);
}
