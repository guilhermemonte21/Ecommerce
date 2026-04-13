package com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Infra.Mappers;

import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Domain.Entity.PedidoDoVendedor;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Domain.Entity.Pedidos;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Domain.Enum.StatusPedido;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Infra.Persistence.Entity.Data.PedidoDoVendedorEntity;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Infra.Persistence.Entity.Data.PedidosEntity;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Infra.Persistence.JpaRepository.JpaPedidosRepository;
import org.springframework.stereotype.Component;

@Component
public class PedidoDoVendedorMapper {

    private final JpaPedidosRepository jpaPedidosRepository;

    public PedidoDoVendedorMapper(JpaPedidosRepository jpaPedidosRepository) {
        this.jpaPedidosRepository = jpaPedidosRepository;
    }

    public PedidoDoVendedor toDomain(PedidoDoVendedorEntity entity) {
        PedidoDoVendedor domain = new PedidoDoVendedor();
        domain.setId(entity.getId());
        domain.setVendedorId(entity.getVendedorId());

        if (entity.getPedido() != null) {
            Pedidos shallowPedido = new Pedidos();
            shallowPedido.setId(entity.getPedido().getId());
            domain.setPedido(shallowPedido);
        }

        domain.setProdutoIds(entity.getProdutoIds());
        domain.setValor(entity.getValor());

        if (entity.getStatus() != null) {
            domain.setStatus(StatusPedido.valueOf(entity.getStatus().name()));
        }

        return domain;
    }

    public PedidoDoVendedorEntity toEntity(PedidoDoVendedor domain) {
        PedidoDoVendedorEntity entity = new PedidoDoVendedorEntity();
        entity.setProdutoIds(domain.getProdutoIds());
        entity.setVendedorId(domain.getVendedorId());

        if (domain.getPedido() != null) {
            if (domain.getPedido().getId() != null) {
                PedidosEntity pedidos = jpaPedidosRepository.getReferenceById(domain.getPedido().getId());
                entity.setPedido(pedidos);
            }
        }

        if (domain.getStatus() != null) {
            entity.setStatus(
                    com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Infra.Persistence.Entity.Enum.StatusPedido
                            .valueOf(domain.getStatus().name()));
        }

        entity.setValor(domain.getValor());
        entity.setId(domain.getId());
        return entity;
    }
}
