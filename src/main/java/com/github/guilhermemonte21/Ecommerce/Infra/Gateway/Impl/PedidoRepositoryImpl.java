package com.github.guilhermemonte21.Ecommerce.Infra.Gateway.Impl;

import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Pedidos;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.PedidoGateway;
import com.github.guilhermemonte21.Ecommerce.Infra.Mappers.PedidoMapper;
import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.Entity.Data.PedidosEntity;
import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.Entity.JpaRepository.JpaPedidosRepository;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class PedidoRepositoryImpl implements PedidoGateway {
    private final JpaPedidosRepository jpaPedidosRepository;
    private final PedidoMapper pedidosMapper;

    public PedidoRepositoryImpl(JpaPedidosRepository jpaPedidosRepository, PedidoMapper pedidosMapper) {
        this.jpaPedidosRepository = jpaPedidosRepository;
        this.pedidosMapper = pedidosMapper;
    }

    @Override
    public Pedidos save(Pedidos pedidosEntity) {
        PedidosEntity newPedido = pedidosMapper.toEntity(pedidosEntity);
        PedidosEntity salvo = jpaPedidosRepository.save(newPedido);
        Pedidos pedidos = pedidosMapper.toDomain(salvo);
        return pedidos;
    }

    @Override
    public Optional<Pedidos> getById(UUID Id) {
        Optional<Pedidos> PedidosById = jpaPedidosRepository.findById(Id).map(pedidosMapper::toDomain);

        return PedidosById;
    }
}
