package com.github.guilhermemonte21.Ecommerce.Infra.Mappers;

import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Pedidos;
import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.Entity.Data.PedidosEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class PedidoMapper {
        private final UsuarioMapper usuarioMapper;
        private final PedidoDoVendedorMapper pedidoDoVendedorMapper;

    public PedidoMapper(UsuarioMapper usuarioMapper, PedidoDoVendedorMapper pedidoDoVendedorMapper) {
        this.usuarioMapper = usuarioMapper;
        this.pedidoDoVendedorMapper = pedidoDoVendedorMapper;
    }

    public Pedidos toDomain(PedidosEntity entity){
        Pedidos newPedido = new Pedidos();
        newPedido.setId(entity.getId());
        newPedido.setPreco(entity.getPreco());
        newPedido.setCriadoEm(entity.getCriadoEm());
        newPedido.setEndereço(entity.getEndereço());
        newPedido.setComprador(usuarioMapper.toDomain(entity.getComprador()));
        newPedido.setItens(new ArrayList<>(entity.getPedidos().stream().map(pedidoDoVendedorMapper::toDomain).toList()));
        return newPedido;
    }

    public PedidosEntity toEntity(Pedidos entity){
        PedidosEntity newPedido = new PedidosEntity();

        newPedido.setId(entity.getId());
        newPedido.setPreco(entity.getPreco());
        newPedido.setEndereço(entity.getEndereço());
        newPedido.setCriadoEm(entity.getCriadoEm());
        newPedido.setComprador(usuarioMapper.toEntity(entity.getComprador()));
        newPedido.setPedidos(new ArrayList<>(entity.getItens().stream().map(pedidoDoVendedorMapper::toEntity).toList()));
        return newPedido;
    }
}
