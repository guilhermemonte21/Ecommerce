package com.github.guilhermemonte21.Ecommerce.Infra.Gateway.Impl;

import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.PedidoDoVendedor;
import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Pedidos;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.PedidoGateway;
import com.github.guilhermemonte21.Ecommerce.Infra.Mappers.PedidoDoVendedorMapper;
import com.github.guilhermemonte21.Ecommerce.Infra.Mappers.PedidoMapper;
import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.Entity.Data.PedidoDoVendedorEntity;
import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.Entity.Data.PedidosEntity;
import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.JpaRepository.JpaPedidosRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class PedidoRepositoryImpl implements PedidoGateway {
    private final JpaPedidosRepository jpaPedidosRepository;
    private final PedidoMapper pedidosMapper;
    private final PedidoDoVendedorMapper pedidoDoVendedorMapper;

    public PedidoRepositoryImpl(JpaPedidosRepository jpaPedidosRepository, PedidoMapper pedidosMapper, PedidoDoVendedorMapper pedidoDoVendedorMapper) {
        this.jpaPedidosRepository = jpaPedidosRepository;
        this.pedidosMapper = pedidosMapper;
        this.pedidoDoVendedorMapper = pedidoDoVendedorMapper;
    }

    @Override
    public Pedidos save(Pedidos pedidosEntity) {
        PedidosEntity newPedido = pedidosMapper.toEntity(pedidosEntity);
        List<PedidoDoVendedorEntity> list = pedidosEntity.getItens().stream().map(pedidoDoVendedorMapper::toEntity).toList();

        newPedido.setPedidos(list);
        PedidosEntity salvo = jpaPedidosRepository.save(newPedido);
        Pedidos pedidos = pedidosMapper.toDomain(salvo);
        return pedidos;
    }

    @Override
    public Optional<Pedidos> getById(UUID Id) {
        Optional<Pedidos> PedidosById = jpaPedidosRepository.findById(Id).map(pedidosMapper::toDomain);

        return PedidosById;
    }

    public List<Pedidos> getPedidosByComprador(UUID IdComprador){
        List<PedidosEntity> pedidoById = jpaPedidosRepository.getPedidosByComprador(IdComprador);

       List<Pedidos> pedidos =  pedidoById.stream().map(pedidosMapper::toDomain).toList();

        return pedidos;
    }
}
