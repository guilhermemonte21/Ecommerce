package com.github.guilhermemonte21.Ecommerce.Infra.Gateway.Impl;

import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Carrinho;
import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Pedidos;
import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Produtos;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.CarrinhoGateway;
import com.github.guilhermemonte21.Ecommerce.Infra.Mappers.CarrinhoMapper;
import com.github.guilhermemonte21.Ecommerce.Infra.Mappers.ProdutoMapper;
import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.Entity.Data.CarrinhoEntity;
import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.Entity.Data.PedidosEntity;
import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.Entity.Data.ProdutosEntity;
import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.Entity.JpaRepository.JpaCarrinhoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CarrinhoRepositoryImpl implements CarrinhoGateway {

    private final JpaCarrinhoRepository jpaCarrinhoRepository;
    private final ProdutoMapper produtoMapper;
    private final CarrinhoMapper carrinhoMapper;

    public CarrinhoRepositoryImpl(JpaCarrinhoRepository jpaCarrinhoRepository, ProdutoMapper produtoMapper, CarrinhoMapper carrinhoMapper) {
        this.jpaCarrinhoRepository = jpaCarrinhoRepository;
        this.produtoMapper = produtoMapper;
        this.carrinhoMapper = carrinhoMapper;
    }

    @Override
    public Carrinho save(Carrinho carrinhoEntity) {
        CarrinhoEntity newCarrinho = carrinhoMapper.toEntity(carrinhoEntity);
        CarrinhoEntity salvo = jpaCarrinhoRepository.save(newCarrinho);
        Carrinho carrinho = carrinhoMapper.toDomain(salvo);
        return carrinho;
    }

    @Override
    public Optional<Carrinho> getById(UUID Id) {
       Optional<Carrinho> CarrinhoById = jpaCarrinhoRepository.findById(Id).map(carrinhoMapper::toDomain);
       return CarrinhoById;
    }

    @Override
    public List<Produtos> add(UUID Id, Produtos produtos) {
        CarrinhoEntity CarrinhobyId = jpaCarrinhoRepository.findById(Id).orElseThrow(() -> new RuntimeException("Carrinho não Encontrado"));


        CarrinhobyId.getItens().add(produtoMapper.toEntity(produtos));

        Carrinho newCarrinho = carrinhoMapper.toDomain(CarrinhobyId);

        return newCarrinho.getItens();
    }
}
