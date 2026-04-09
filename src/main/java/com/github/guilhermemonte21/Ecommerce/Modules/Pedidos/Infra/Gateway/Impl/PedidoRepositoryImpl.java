package com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Infra.Gateway.Impl;

import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Domain.Entity.Pedidos;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.Gateway.PedidoGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Infra.Mappers.PedidoMapper;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Infra.Persistence.Entity.Data.PedidosEntity;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Infra.Persistence.JpaRepository.JpaPedidosRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class PedidoRepositoryImpl implements PedidoGateway {
    private final JpaPedidosRepository jpaPedidosRepository;
    private final PedidoMapper pedidosMapper;

    public PedidoRepositoryImpl(JpaPedidosRepository jpaPedidosRepository, PedidoMapper pedidosMapper) {
        this.jpaPedidosRepository = jpaPedidosRepository;
        this.pedidosMapper = pedidosMapper;
    }

    @Override
    @Transactional
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

    public List<Pedidos> getPedidosByComprador(UUID IdComprador) {
        List<PedidosEntity> pedidoById = jpaPedidosRepository.getPedidosByComprador(IdComprador);

        List<Pedidos> pedidos = pedidoById.stream().map(pedidosMapper::toDomain).toList();

        return pedidos;
    }

    @Override
    public List<Pedidos> getPedidosByStatusAndCriadoEmBefore(com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Domain.Enum.StatusPedido status, java.time.OffsetDateTime threshold) {
        com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Infra.Persistence.Entity.Enum.StatusPedido entityStatus = 
                com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Infra.Persistence.Entity.Enum.StatusPedido.valueOf(status.name());
        
        return jpaPedidosRepository.findByStatusAndCriadoEmBefore(entityStatus, threshold)
                .stream()
                .map(pedidosMapper::toDomain)
                .toList();
    }
}
