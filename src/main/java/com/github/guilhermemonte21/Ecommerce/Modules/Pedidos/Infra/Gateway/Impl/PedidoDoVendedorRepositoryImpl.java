package com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Infra.Gateway.Impl;

import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.Gateway.PedidoDoVendedorGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Domain.Entity.PedidoDoVendedor;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Infra.Mappers.PedidoDoVendedorMapper;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Infra.Persistence.Entity.Data.PedidoDoVendedorEntity;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Infra.Persistence.JpaRepository.JpaPedidoDoVendedorRepo;
import org.springframework.stereotype.Component;

@Component
public class PedidoDoVendedorRepositoryImpl implements PedidoDoVendedorGateway {
    private final JpaPedidoDoVendedorRepo pedidoDoVendedorRepo;
    private final PedidoDoVendedorMapper mapper;

    public PedidoDoVendedorRepositoryImpl(JpaPedidoDoVendedorRepo pedidoDoVendedorRepo, PedidoDoVendedorMapper mapper) {
        this.pedidoDoVendedorRepo = pedidoDoVendedorRepo;
        this.mapper = mapper;
    }

    @Override
    public PedidoDoVendedor save(PedidoDoVendedor pedidoDoVendedor) {
        PedidoDoVendedorEntity entity = mapper.toEntity(pedidoDoVendedor);
        PedidoDoVendedorEntity salvo = pedidoDoVendedorRepo.save(entity);
        return mapper.toDomain(salvo);
    }
}
