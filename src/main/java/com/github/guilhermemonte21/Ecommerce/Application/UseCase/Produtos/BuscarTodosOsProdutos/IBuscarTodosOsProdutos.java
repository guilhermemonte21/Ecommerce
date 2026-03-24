package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Produtos.BuscarTodosOsProdutos;

import com.github.guilhermemonte21.Ecommerce.Application.DTO.Produtos.ProdutoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IBuscarTodosOsProdutos {
    Page<ProdutoResponse> findAll(Pageable pageable);
}
