package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Produtos.AtualizarProduto;

import com.github.guilhermemonte21.Ecommerce.Application.DTO.Produtos.CreateProdutoRequest;
import com.github.guilhermemonte21.Ecommerce.Application.DTO.Produtos.ProdutoResponse;

import java.util.UUID;

public interface IAtualizarProduto {
    ProdutoResponse Atualizar(UUID IdProduto, CreateProdutoRequest produtos);
}
