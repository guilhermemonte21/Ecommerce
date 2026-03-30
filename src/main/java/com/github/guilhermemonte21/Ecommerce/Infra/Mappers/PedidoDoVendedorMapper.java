package com.github.guilhermemonte21.Ecommerce.Infra.Mappers;

import com.github.guilhermemonte21.Ecommerce.Domain.Entity.PedidoDoVendedor;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Pedidos;
import com.github.guilhermemonte21.Ecommerce.Domain.Enum.StatusPedido;
import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.Entity.Data.PedidoDoVendedorEntity;
import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.Entity.Data.PedidosEntity;
import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.JpaRepository.JpaPedidosRepository;
import org.springframework.stereotype.Component;

@Component
public class PedidoDoVendedorMapper {
    private final UsuarioMapper usuarioMapper;
    private final ProdutoMapper produtoMapper;
    private final JpaPedidosRepository jpaPedidosRepository;
    public PedidoDoVendedorMapper(UsuarioMapper usuarioMapper, ProdutoMapper produtoMapper,
            JpaPedidosRepository jpaPedidosRepository) {
        this.usuarioMapper = usuarioMapper;
        this.produtoMapper = produtoMapper;
        this.jpaPedidosRepository = jpaPedidosRepository;
    }

    public PedidoDoVendedor toDomain(PedidoDoVendedorEntity entity) {
        PedidoDoVendedor domain = new PedidoDoVendedor();
        domain.setId(entity.getId());
        domain.setVendedor(usuarioMapper.toDomain(entity.getVendedor()));
        
        if (entity.getPedido() != null) {
            Pedidos shallowPedido = new Pedidos();
            shallowPedido.setId(entity.getPedido().getId());
            domain.setPedido(shallowPedido);
        }

        domain.setProdutos(entity.getProdutos().stream().map(produtoMapper::toDomain).toList());
        domain.setValor(entity.getValor());

        if (entity.getStatus() != null) {
            domain.setStatus(StatusPedido.valueOf(entity.getStatus().name()));
        }

        return domain;
    }

    public PedidoDoVendedorEntity toEntity(PedidoDoVendedor domain) {
        PedidoDoVendedorEntity entity = new PedidoDoVendedorEntity();
        entity.setProdutos(domain.getProdutos().stream().map(produtoMapper::toEntity).toList());
        entity.setVendedor(usuarioMapper.toEntity(domain.getVendedor()));

        if (domain.getPedido() != null) {
            PedidosEntity pedidos = jpaPedidosRepository.getReferenceById(domain.getPedido().getId());
            entity.setPedido(pedidos);
        }

        if (domain.getStatus() != null) {
            entity.setStatus(com.github.guilhermemonte21.Ecommerce.Infra.Persistence.Entity.Enum.StatusPedido
                    .valueOf(domain.getStatus().name()));
        }

        entity.setValor(domain.getValor());
        entity.setId(domain.getId());
        return entity;
    }
}
