package com.github.guilhermemonte21.Ecommerce.Infra.Mappers;

import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Pedidos;
import com.github.guilhermemonte21.Ecommerce.Domain.Enum.StatusPedido;
import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.Entity.Data.PedidoDoVendedorEntity;
import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.Entity.Data.PedidosEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PedidoMapper {
    private final UsuarioMapper usuarioMapper;
    private final PedidoDoVendedorMapper pedidoDoVendedorMapper;

    public PedidoMapper(UsuarioMapper usuarioMapper, PedidoDoVendedorMapper pedidoDoVendedorMapper) {
        this.usuarioMapper = usuarioMapper;
        this.pedidoDoVendedorMapper = pedidoDoVendedorMapper;
    }

    public Pedidos toDomain(PedidosEntity entity) {
        Pedidos newPedido = new Pedidos();
        newPedido.setId(entity.getId());
        newPedido.setPreco(entity.getPreco());
        newPedido.setCriadoEm(entity.getCriadoEm());
        newPedido.setEndereco(entity.getEndereco());
        newPedido.setComprador(usuarioMapper.toDomain(entity.getComprador()));
        newPedido
                .setItens(new ArrayList<>(entity.getPedidos().stream().map(pedidoDoVendedorMapper::toDomain).toList()));
        if (entity.getStatus() != null) {
            newPedido.setStatus(StatusPedido.valueOf(entity.getStatus().name()));
        }
        return newPedido;
    }

    public PedidosEntity toEntity(Pedidos entity) {
        PedidosEntity newPedido = new PedidosEntity();

        newPedido.setId(entity.getId());
        newPedido.setPreco(entity.getPreco());
        newPedido.setEndereco(entity.getEndereco());
        newPedido.setCriadoEm(entity.getCriadoEm());
        newPedido.setComprador(usuarioMapper.toEntity(entity.getComprador()));
        List<PedidoDoVendedorEntity> childs = new ArrayList<>(
                entity.getItens().stream().map(pedidoDoVendedorMapper::toEntity).toList());
        for (PedidoDoVendedorEntity child : childs) {
            child.setPedido(newPedido);
        }
        newPedido.setPedidos(childs);
        if (entity.getStatus() != null) {
            newPedido.setStatus(com.github.guilhermemonte21.Ecommerce.Infra.Persistence.Entity.Enum.StatusPedido
                    .valueOf(entity.getStatus().name()));
        }
        return newPedido;
    }
}
