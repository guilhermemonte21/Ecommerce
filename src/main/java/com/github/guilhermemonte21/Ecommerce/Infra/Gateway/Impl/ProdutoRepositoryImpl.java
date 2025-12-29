package com.github.guilhermemonte21.Ecommerce.Infra.Gateway.Impl;

import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Produtos;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.ProdutoGateway;

import java.util.Optional;
import java.util.UUID;

public class ProdutoRepositoryImpl implements ProdutoGateway {

    @Override
    public void salvar(Produtos produtosEntity) {

    }

    @Override
    public Optional<Produtos> GetById(UUID Id) {
        return Optional.empty();
    }

    @Override
    public void Delete(Produtos produtosEntity) {

    }
}
