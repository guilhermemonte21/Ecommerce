package com.github.guilhermemonte21.Ecommerce.Infra.Gateway.Impl;

import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Carrinho;
import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Produtos;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.CarrinhoGateway;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CarrinhoRepositoryImpl implements CarrinhoGateway {

    @Override
    public void save(Carrinho carrinhoEntity) {

    }

    @Override
    public Optional<Carrinho> getById(UUID Id) {
        return Optional.empty();
    }

    @Override
    public List<Produtos> add(Produtos produtos) {
        return List.of();
    }
}
