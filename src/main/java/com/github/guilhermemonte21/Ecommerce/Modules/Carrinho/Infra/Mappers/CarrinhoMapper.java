package com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Infra.Mappers;

import com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Domain.Entity.Carrinho;
import com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Infra.Persistence.Entity.Data.CarrinhoEntity;
import org.springframework.stereotype.Component;

@Component
public class CarrinhoMapper {
    public CarrinhoMapper() {
    }

    public Carrinho toDomain(CarrinhoEntity entity) {
        Carrinho carrinho = new Carrinho();
        carrinho.setId(entity.getId());
        carrinho.setCompradorId(entity.getCompradorId());
        carrinho.setProdutoIds(entity.getProdutoIds());
        carrinho.setValorTotal(entity.getValorTotal());
        carrinho.setAtualizadoEm(entity.getAtualizadoEm());
        return carrinho;
    }

    public CarrinhoEntity toEntity(Carrinho domain) {
        CarrinhoEntity entity = new CarrinhoEntity();
        entity.setId(domain.getId());
        entity.setCompradorId(domain.getCompradorId());
        entity.setProdutoIds(domain.getProdutoIds());
        entity.setValorTotal(domain.getValorTotal());
        entity.setAtualizadoEm(domain.getAtualizadoEm());
        return entity;
    }
}
