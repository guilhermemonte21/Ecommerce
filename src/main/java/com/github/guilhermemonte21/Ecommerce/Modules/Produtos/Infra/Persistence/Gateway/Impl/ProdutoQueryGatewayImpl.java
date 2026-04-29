package com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Infra.Persistence.Gateway.Impl;

import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.DTO.Produtos.ProdutoResponse;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.Gateway.IProdutoQueryGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.Mappers.ProdutoMapperApl;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Infra.Persistence.Elasticsearch.Repository.ProductElasticRepository;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions.ProdutoNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ProdutoQueryGatewayImpl implements IProdutoQueryGateway {

    private final ProductElasticRepository repository;
    private final ProdutoMapperApl mapper;

    public ProdutoQueryGatewayImpl(ProductElasticRepository repository, ProdutoMapperApl mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Page<ProdutoResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Override
    public ProdutoResponse getById(UUID id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new ProdutoNotFoundException(id));
    }

    @Override
    public Page<ProdutoResponse> search(String query, Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }
}
