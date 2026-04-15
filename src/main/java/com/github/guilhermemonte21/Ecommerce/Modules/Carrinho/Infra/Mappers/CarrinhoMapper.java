package com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Infra.Mappers;

import com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Domain.Entity.Carrinho;
import com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Domain.Entity.CarrinhoItem;
import com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Infra.Persistence.Entity.Data.CarrinhoEntity;
import org.springframework.stereotype.Component;

import com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Infra.Persistence.Entity.Data.CarrinhoItemEntity;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CarrinhoMapper {
        public CarrinhoMapper() {
        }

        public Carrinho toDomain(CarrinhoEntity entity) {
                Carrinho carrinho = new Carrinho();
                carrinho.setId(entity.getId());
                carrinho.setCompradorId(entity.getCompradorId());
                carrinho.setItens(entity.getItens().stream()
                                .map(item -> new CarrinhoItem(
                                                item.getProdutoId(),
                                                item.getNomeProduto(),
                                                item.getPreco(),
                                                item.getQuantidade()))
                                .collect(Collectors.toList()));
                carrinho.setValorTotal(entity.getValorTotal());
                carrinho.setAtualizadoEm(entity.getAtualizadoEm());
                return carrinho;
        }

        public CarrinhoEntity toEntity(Carrinho domain) {
                CarrinhoEntity entity = new CarrinhoEntity();
                entity.setId(domain.getId());
                entity.setCompradorId(domain.getCompradorId());

                List<CarrinhoItemEntity> itemEntities = domain.getItens().stream()
                                .map(item -> new CarrinhoItemEntity(
                                                entity,
                                                item.getProdutoId(),
                                                item.getNomeProduto(),
                                                item.getPreco(),
                                                item.getQuantidade()))
                                .collect(Collectors.toList());

                entity.setItens(itemEntities);
                entity.setValorTotal(domain.getValorTotal());
                entity.setAtualizadoEm(domain.getAtualizadoEm());
                return entity;
        }
}
