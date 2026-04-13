package com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Infra.Mappers;

import org.springframework.stereotype.Component;

import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Domain.Entity.Produtos;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Infra.Persistence.Entity.Data.ProdutosEntity;

@Component
public class ProdutoMapper {
    public ProdutoMapper() {
    }

    public Produtos toDomain(ProdutosEntity entity) {
        Produtos produtos = new Produtos();
        produtos.setId(entity.getId());
        produtos.setNomeProduto(entity.getNomeProduto());
        produtos.setPreco(entity.getPreco());
        produtos.setEstoque(entity.getEstoque());
        produtos.setVendedorId(entity.getVendedorId());
        produtos.setDescricao(entity.getDescricao());
        produtos.setVersion(entity.getVersion());
        return produtos;
    }

    public ProdutosEntity toEntity(Produtos domain) {
        ProdutosEntity entity = new ProdutosEntity();
        entity.setId(domain.getId());
        entity.setNomeProduto(domain.getNomeProduto());
        entity.setPreco(domain.getPreco());
        entity.setEstoque(domain.getEstoque());
        entity.setDescricao(domain.getDescricao());
        entity.setVersion(domain.getVersion());

        entity.setVendedorId(domain.getVendedorId());
        return entity;
    }
}
