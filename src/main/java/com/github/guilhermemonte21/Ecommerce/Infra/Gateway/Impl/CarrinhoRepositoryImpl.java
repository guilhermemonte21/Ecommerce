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
import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.Entity.JpaRepository.JpaProdutosRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class CarrinhoRepositoryImpl implements CarrinhoGateway {

    private final JpaCarrinhoRepository jpaCarrinhoRepository;
    private final ProdutoMapper produtoMapper;
    private final CarrinhoMapper carrinhoMapper;
    private final JpaProdutosRepository jpaProdutosRepository;

    public CarrinhoRepositoryImpl(JpaCarrinhoRepository jpaCarrinhoRepository, ProdutoMapper produtoMapper, CarrinhoMapper carrinhoMapper, JpaProdutosRepository jpaProdutosRepository) {
        this.jpaCarrinhoRepository = jpaCarrinhoRepository;
        this.produtoMapper = produtoMapper;
        this.carrinhoMapper = carrinhoMapper;
        this.jpaProdutosRepository = jpaProdutosRepository;
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
    public Carrinho add(UUID Id, UUID IdProduto) {
        CarrinhoEntity CarrinhobyId = jpaCarrinhoRepository.findById(Id).orElseThrow(() -> new RuntimeException("Carrinho não Encontrado"));

        ProdutosEntity produtos = jpaProdutosRepository.findById(IdProduto).orElseThrow(() -> new RuntimeException("Produto não Encontrado"));
        CarrinhobyId.getItens().add(produtos);

        Carrinho newCarrinho = carrinhoMapper.toDomain(CarrinhobyId);

        return newCarrinho;
    }

    @Override
    public void DeleteItem(Carrinho carrinho, UUID id) {
       CarrinhoEntity carrinhoEntity = carrinhoMapper.toEntity(carrinho);
       ProdutosEntity produtosEntity = jpaProdutosRepository.findById(id).orElseThrow(() -> new RuntimeException("Produto não está presente no Carrinho"));
        carrinhoEntity.getItens().remove(produtosEntity);


    }
}
