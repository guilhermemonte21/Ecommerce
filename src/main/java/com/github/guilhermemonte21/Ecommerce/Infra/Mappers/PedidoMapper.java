package com.github.guilhermemonte21.Ecommerce.Infra.Mappers;

import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Pedidos;
import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.Entity.Data.PedidosEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class PedidoMapper {
        private final ProdutoMapper produtoMapper;
        private final UsuarioMapper usuarioMapper;
        private final PedidoSellerMapper pedidoSellerMapper;

    public PedidoMapper(ProdutoMapper produtoMapper, UsuarioMapper usuarioMapper, PedidoSellerMapper pedidoSellerMapper) {
        this.produtoMapper = produtoMapper;
        this.usuarioMapper = usuarioMapper;
        this.pedidoSellerMapper = pedidoSellerMapper;
    }

    public Pedidos toDomain(PedidosEntity entity){
        Pedidos newPedido = new Pedidos();
        newPedido.setId(entity.getId());
        newPedido.setPreco(entity.getPreco());
        newPedido.setCriadoEm(entity.getCriadoEm());
        newPedido.setItens(entity.getPedidos().stream().map(c ->
                pedidoSellerMapper.toDomain(c)).collect(Collectors.toList()));
        newPedido.setComprador(usuarioMapper.toDomain(entity.getComprador()));

        return newPedido;
    }

    public PedidosEntity toEntity(Pedidos entity){
        PedidosEntity newPedido = new PedidosEntity();
        newPedido.setId(entity.getId());
        newPedido.setPreco(entity.getPreco());
        newPedido.setCriadoEm(entity.getCriadoEm());
        newPedido.setPedidos(entity.getItens().stream().map(c ->
                pedidoSellerMapper.toEntity(c)).collect(Collectors.toList()));
        newPedido.setComprador(usuarioMapper.toEntity(entity.getComprador()));
        return newPedido;
    }
}
