package com.github.guilhermemonte21.Ecommerce.Infra.Mappers;

import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Pedidos;
import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.Entity.Data.PedidosEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class PedidoMapper {
        private final ProdutoMapper produtoMapper;
        private final UsuarioMapper usuarioMapper;

    public PedidoMapper(ProdutoMapper produtoMapper, UsuarioMapper usuarioMapper) {
        this.produtoMapper = produtoMapper;
        this.usuarioMapper = usuarioMapper;
    }

    public Pedidos toDomain(PedidosEntity entity){
        Pedidos newPedido = new Pedidos();
        newPedido.setId(entity.getId());
        newPedido.setPreco(entity.getPreco());
        newPedido.setCriadoEm(entity.getCriadoEm());
        newPedido.setItens(entity.getItens().stream().map(c ->
                produtoMapper.toDomain(c)).collect(Collectors.toList()));
        newPedido.setComprador(usuarioMapper.toDomain(entity.getComprador()));
        newPedido.setVendedor(usuarioMapper.toDomain(entity.getVendedor()));
        return newPedido;
    }

    public PedidosEntity toEntity(Pedidos entity){
        PedidosEntity newPedido = new PedidosEntity();
        newPedido.setId(entity.getId());
        newPedido.setPreco(entity.getPreco());
        newPedido.setCriadoEm(entity.getCriadoEm());
        newPedido.setItens(entity.getItens().stream().map(c ->
                produtoMapper.toEntity(c)).collect(Collectors.toList()));
        newPedido.setComprador(usuarioMapper.toEntity(entity.getComprador()));
        newPedido.setVendedor(usuarioMapper.toEntity(entity.getVendedor()));
        return newPedido;
    }
}
