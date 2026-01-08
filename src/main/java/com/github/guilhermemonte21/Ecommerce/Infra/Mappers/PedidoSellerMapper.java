package com.github.guilhermemonte21.Ecommerce.Infra.Mappers;

import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.PedidosSeller;
import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.Entity.Data.PedidoSellerEntity;
import org.springframework.stereotype.Component;

@Component
public class PedidoSellerMapper {
    private final ProdutoMapper produtoMapper;
    private final UsuarioMapper usuarioMapper;
    private final PedidoMapper pedidoMapper;

    public PedidoSellerMapper(ProdutoMapper produtoMapper, UsuarioMapper usuarioMapper, PedidoMapper pedidoMapper) {
        this.produtoMapper = produtoMapper;
        this.usuarioMapper = usuarioMapper;
        this.pedidoMapper = pedidoMapper;
    }

    public PedidosSeller toDomain(PedidoSellerEntity entity){
        PedidosSeller domain = new PedidosSeller();
        domain.setProdutos(produtoMapper.toDomain(entity.getProdutos()));
        domain.setId(entity.getId());
        domain.setSeller(usuarioMapper.toDomain(entity.getSeller()));
        domain.setStatus(entity.getStatus());
        domain.setValorTotal(entity.getValorTotal());
        domain.setPedido(pedidoMapper.toDomain(entity.getPedido()));
        return domain;

    }

    public PedidoSellerEntity toEntity(PedidosSeller domain) {
            PedidoSellerEntity entity = new PedidoSellerEntity();

            entity.setProdutos(produtoMapper.toEntity(domain.getProdutos()));
            entity.setPedido(pedidoMapper.toEntity(domain.getPedido()));
            entity.setStatus(domain.getStatus());
            entity.setId(domain.getId());
            entity.setValorTotal(domain.getValorTotal());
            entity.setSeller(usuarioMapper.toEntity(domain.getSeller()));
            return entity;


    }

}
