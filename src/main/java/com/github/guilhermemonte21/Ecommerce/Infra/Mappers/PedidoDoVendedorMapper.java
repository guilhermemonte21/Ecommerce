package com.github.guilhermemonte21.Ecommerce.Infra.Mappers;

import com.github.guilhermemonte21.Ecommerce.Application.Gateway.PedidoGateway;
import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.PedidoDoVendedor;
import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Produtos;
import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.Entity.Data.PedidoDoVendedorEntity;
import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.Entity.Data.ProdutosEntity;
import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.Entity.Data.UsuariosEntity;
import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.JpaRepository.JpaPedidosRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class PedidoDoVendedorMapper {
    private final UsuarioMapper usuarioMapper;
    private final PedidoMapper pedidoMapper;
    private final ProdutoMapper produtoMapper;

    public PedidoDoVendedorMapper(UsuarioMapper usuarioMapper, PedidoMapper pedidoMapper, ProdutoMapper produtoMapper) {
        this.usuarioMapper = usuarioMapper;
        this.pedidoMapper = pedidoMapper;
        this.produtoMapper = produtoMapper;
    }

    public PedidoDoVendedor toDomain(PedidoDoVendedorEntity entity){
     PedidoDoVendedor domain = new PedidoDoVendedor();
      domain.setId(entity.getId());
      domain.setVendedor(usuarioMapper.toDomain(entity.getVendedor()));
      domain.setPedido(pedidoMapper.toDomain(entity.getPedido()));
      domain.setProdutos(entity.getProdutos().stream().map(produtoMapper::toDomain).toList());
      domain.setValor(entity.getValor());
      domain.setStatus(entity.getStatus());

      return domain;

    }

    public PedidoDoVendedorEntity toEntity(PedidoDoVendedor domain) {
            PedidoDoVendedorEntity entity = new PedidoDoVendedorEntity();
            entity.setProdutos(domain.getProdutos().stream().map(produtoMapper::toEntity).toList());
            entity.setVendedor(usuarioMapper.toEntity(domain.getVendedor()));
            entity.setStatus(domain.getStatus());
            entity.setValor(domain.getValor());
            entity.setId(domain.getId());
            entity.setPedido(pedidoMapper.toEntity(domain.getPedido()));
            return entity;

    }
}
