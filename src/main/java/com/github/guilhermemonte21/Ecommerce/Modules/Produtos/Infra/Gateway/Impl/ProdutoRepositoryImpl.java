package com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Infra.Gateway.Impl;

import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Domain.Entity.Produtos;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.Gateway.ProdutoGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Infra.Mappers.ProdutoMapper;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Infra.Persistence.Entity.Data.ProdutosEntity;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Infra.Persistence.JpaRepository.JpaProdutosRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@org.springframework.transaction.annotation.Transactional
public class ProdutoRepositoryImpl implements ProdutoGateway {

    private final JpaProdutosRepository jpaProdutosRepo;
    private final ProdutoMapper mapper;

    public ProdutoRepositoryImpl(JpaProdutosRepository jpaProdutosRepo, ProdutoMapper mapper) {
        this.jpaProdutosRepo = jpaProdutosRepo;

        this.mapper = mapper;
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public Produtos salvar(Produtos produtos) {
        ProdutosEntity produtosEntity = mapper.toEntity(produtos);
        ProdutosEntity salvo = jpaProdutosRepo.save(produtosEntity);
        return mapper.toDomain(salvo);
    }

    @Override
    public Optional<Produtos> getById(UUID id) {
        return jpaProdutosRepo.findById(id).map(mapper::toDomain);
    }

    @Override
    public void delete(Produtos produtos) {
        ProdutosEntity produtosEntity = mapper.toEntity(produtos);
        jpaProdutosRepo.delete(produtosEntity);
    }

    @Override
    public Page<Produtos> findAll(Pageable pageable) {
        return jpaProdutosRepo.findAll(pageable).map(mapper::toDomain);
    }

    @Override
    public Optional<Produtos> getByIdComLock(UUID id) {
        return jpaProdutosRepo.findByIdWithLock(id).map(mapper::toDomain);
    }
}
