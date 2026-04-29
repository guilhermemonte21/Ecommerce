package com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.Gateway;

import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.DTO.Produtos.ProdutoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IProdutoQueryGateway {
    Page<ProdutoResponse> findAll(Pageable pageable);
    ProdutoResponse getById(UUID id);
    Page<ProdutoResponse> search(String query, Pageable pageable);
}
