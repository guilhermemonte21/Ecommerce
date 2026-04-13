package com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Infra.Gateway.Impl;

import com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions.CarrinhoNotFoundException;
import com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Domain.Entity.Carrinho;
import com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Application.Gateway.CarrinhoGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Infra.Mappers.CarrinhoMapper;
import com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Infra.Persistence.Entity.Data.CarrinhoEntity;
import com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Infra.Persistence.JpaRepository.JpaCarrinhoRepository;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Component
@Transactional
public class CarrinhoRepositoryImpl implements CarrinhoGateway {

    private final JpaCarrinhoRepository jpaCarrinhoRepository;
    private final CarrinhoMapper carrinhoMapper;

    public CarrinhoRepositoryImpl(JpaCarrinhoRepository jpaCarrinhoRepository, CarrinhoMapper carrinhoMapper) {
        this.jpaCarrinhoRepository = jpaCarrinhoRepository;
        this.carrinhoMapper = carrinhoMapper;
    }

    @Override
    public Carrinho save(Carrinho carrinhoEntity) {
        CarrinhoEntity newCarrinho = carrinhoMapper.toEntity(carrinhoEntity);
        CarrinhoEntity salvo = jpaCarrinhoRepository.save(newCarrinho);
        return carrinhoMapper.toDomain(salvo);
    }

    @Override
    public Optional<Carrinho> getById(UUID id) {
        return jpaCarrinhoRepository.findById(id).map(carrinhoMapper::toDomain);
    }

    @Override
    public void deleteItem(Carrinho carrinho, UUID id) {
        CarrinhoEntity carrinhoEntity = jpaCarrinhoRepository.findById(carrinho.getId())
                .orElseThrow(() -> new CarrinhoNotFoundException(carrinho.getId()));
        carrinhoEntity.getProdutoIds().remove(id);
        jpaCarrinhoRepository.save(carrinhoEntity);
    }

    @Override
    public Carrinho getByDono(UUID id) {
        CarrinhoEntity cart = jpaCarrinhoRepository.findByCompradorId(id);
        if (cart == null) {
            return null;
        }
        return carrinhoMapper.toDomain(cart);
    }
}
