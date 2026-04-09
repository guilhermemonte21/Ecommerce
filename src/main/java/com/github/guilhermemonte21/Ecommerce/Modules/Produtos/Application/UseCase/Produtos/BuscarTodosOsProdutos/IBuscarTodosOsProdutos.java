package com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.UseCase.Produtos.BuscarTodosOsProdutos;

import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.DTO.Produtos.ProdutoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IBuscarTodosOsProdutos {
    Page<ProdutoResponse> findAll(Pageable pageable);
}
