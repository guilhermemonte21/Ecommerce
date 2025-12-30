package com.github.guilhermemonte21.Ecommerce.Infra.Gateway.Impl;

import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Produtos;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.ProdutoGateway;
import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Usuarios;
import com.github.guilhermemonte21.Ecommerce.Infra.Mappers.ProdutoMapper;
import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.Entity.Data.ProdutosEntity;
import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.Entity.Data.UsuariosEntity;
import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.Entity.JpaRepository.JpaProdutosRepository;
import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.Entity.JpaRepository.JpaUsuarioRepository;

import java.util.Optional;
import java.util.UUID;

public class ProdutoRepositoryImpl implements ProdutoGateway {

    private final JpaProdutosRepository JpaProdutosRepo;
    private final JpaUsuarioRepository JpaUsuarioRepo;
    private final ProdutoMapper mapper;

    public ProdutoRepositoryImpl(JpaProdutosRepository jpaProdutosRepo, JpaUsuarioRepository jpaUsuarioRepo, ProdutoMapper mapper) {
        JpaProdutosRepo = jpaProdutosRepo;
        JpaUsuarioRepo = jpaUsuarioRepo;
        this.mapper = mapper;
    }

    @Override
    public Produtos salvar(Produtos produtos) {
        ProdutosEntity produtosEntity = mapper.toEntity(produtos);

        ProdutosEntity salvo = JpaProdutosRepo.save(produtosEntity);

        Produtos newProduto = mapper.toDomain(salvo);

        return newProduto;
    }

    @Override
    public Optional<Produtos> GetById(UUID Id) {
        Optional<Produtos> entity = JpaProdutosRepo.findById(Id).map(mapper::toDomain);
               return entity;
    }

    @Override
    public void Delete(Produtos produtos) {
        ProdutosEntity produtosEntity = JpaProdutosRepo.findById(produtos.getId()).orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        JpaProdutosRepo.delete(produtosEntity);
    }
}



