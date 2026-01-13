package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Produtos.BuscarTodosOsProdutos;

import com.github.guilhermemonte21.Ecommerce.Application.DTO.Produtos.ProdutoResponse;

import java.util.List;

public interface IBuscarTodosOsProdutos {
    List<ProdutoResponse> findAll();
}
