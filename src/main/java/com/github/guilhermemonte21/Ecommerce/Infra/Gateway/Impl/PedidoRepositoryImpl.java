package com.github.guilhermemonte21.Ecommerce.Infra.Gateway.Impl;

import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Pedidos;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.PedidoGateway;

import java.util.Optional;
import java.util.UUID;

public class PedidoRepositoryImpl implements PedidoGateway {

    @Override
    public void save(Pedidos pedidosEntity) {

    }

    @Override
    public Optional<Pedidos> getById(UUID Id) {
        return Optional.empty();
    }
}
