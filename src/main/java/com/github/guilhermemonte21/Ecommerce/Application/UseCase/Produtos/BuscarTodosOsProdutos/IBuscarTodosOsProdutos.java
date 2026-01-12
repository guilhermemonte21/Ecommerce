package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Produtos.BuscarTodosOsProdutos;

import com.github.guilhermemonte21.Ecommerce.Application.DTO.Produtos.ProdutoResponse;
import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Produtos;

import java.util.List;

public interface IBuscarTodosOsProdutos {
    List<ProdutoResponse> findAll();
}
