package com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.UseCase.Produtos.AtualizarProduto;

import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.DTO.Produtos.CreateProdutoRequest;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.DTO.Produtos.ProdutoResponse;

import java.util.UUID;

public interface IAtualizarProduto {
    ProdutoResponse atualizar(UUID idProduto, CreateProdutoRequest produtos);
}
