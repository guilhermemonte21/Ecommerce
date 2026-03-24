package com.github.guilhermemonte21.Ecommerce.Infra.Gateway.Impl;

import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.CarrinhoNotFoundException;
import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.ProdutoNotFoundException;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Carrinho;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Produtos;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.CarrinhoGateway;
import com.github.guilhermemonte21.Ecommerce.Infra.Mappers.CarrinhoMapper;
import com.github.guilhermemonte21.Ecommerce.Infra.Mappers.ProdutoMapper;
import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.Entity.Data.CarrinhoEntity;
import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.Entity.Data.ProdutosEntity;
import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.JpaRepository.JpaCarrinhoRepository;
import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.JpaRepository.JpaProdutosRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class CarrinhoRepositoryImpl implements CarrinhoGateway {

    private final JpaCarrinhoRepository jpaCarrinhoRepository;
    private final ProdutoMapper produtoMapper;
    private final CarrinhoMapper carrinhoMapper;
    private final JpaProdutosRepository jpaProdutosRepository;

    public CarrinhoRepositoryImpl(JpaCarrinhoRepository jpaCarrinhoRepository, ProdutoMapper produtoMapper,
                                  CarrinhoMapper carrinhoMapper, JpaProdutosRepository jpaProdutosRepository) {
        this.jpaCarrinhoRepository = jpaCarrinhoRepository;
        this.produtoMapper = produtoMapper;
        this.carrinhoMapper = carrinhoMapper;
        this.jpaProdutosRepository = jpaProdutosRepository;
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
    public Carrinho add(UUID id, Produtos produtos, Long quantity) {
        CarrinhoEntity carrinhoById = jpaCarrinhoRepository.findById(id)
                .orElseThrow(() -> new CarrinhoNotFoundException(id));
        ProdutosEntity entity = produtoMapper.toEntity(produtos);
        for (int i = 0; i < quantity; i++) {
            carrinhoById.getItens().add(entity);
        }
        return carrinhoMapper.toDomain(carrinhoById);
    }

    @Override
    public void deleteItem(Carrinho carrinho, UUID id) {
        CarrinhoEntity carrinhoEntity = jpaCarrinhoRepository.findById(carrinho.getId())
                .orElseThrow(() -> new CarrinhoNotFoundException(carrinho.getId()));
        ProdutosEntity produtosEntity = jpaProdutosRepository.findById(id)
                .orElseThrow(() -> new ProdutoNotFoundException(id));
        carrinhoEntity.getItens().remove(produtosEntity);
        jpaCarrinhoRepository.save(carrinhoEntity);
    }

    @Override
    public void limparCarrinho(Carrinho cart) {
        cart.limpar();
        cart.atualizarValorTotal();
        cart.atualizadoAgora();
        CarrinhoEntity carrinho = carrinhoMapper.toEntity(cart);
        jpaCarrinhoRepository.save(carrinho);
    }
}
