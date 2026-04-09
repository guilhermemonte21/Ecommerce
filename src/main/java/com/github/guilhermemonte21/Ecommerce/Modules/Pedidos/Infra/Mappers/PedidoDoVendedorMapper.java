package com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Infra.Mappers;

import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Infra.Mappers.UsuarioMapper;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Infra.Mappers.ProdutoMapper;

import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Domain.Entity.PedidoDoVendedor;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Domain.Entity.Pedidos;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Domain.Enum.StatusPedido;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Infra.Persistence.Entity.Data.PedidoDoVendedorEntity;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Infra.Persistence.Entity.Data.PedidosEntity;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Infra.Persistence.JpaRepository.JpaPedidosRepository;
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

        if (domain.getVendedor() != null && domain.getVendedor().getId() != null) {
            entity.setVendedor(
                    new com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Infra.Persistence.Entity.Data.UsuariosEntity());
            entity.getVendedor().setId(domain.getVendedor().getId());
            entity.setVendedor(usuarioMapper.toEntity(domain.getVendedor()));
        }

        if (domain.getPedido() != null) {
            if (domain.getPedido().getId() != null) {
                PedidosEntity pedidos = jpaPedidosRepository.getReferenceById(domain.getPedido().getId());
                entity.setPedido(pedidos);
            }
        }

        if (domain.getStatus() != null) {
            entity.setStatus(com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Infra.Persistence.Entity.Enum.StatusPedido
                    .valueOf(domain.getStatus().name()));
        }

        entity.setValor(domain.getValor());
        entity.setId(domain.getId());
        return entity;
    }
}
