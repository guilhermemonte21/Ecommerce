package com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.UseCase.Produtos.RegistrarProduto;

import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.DTO.Produtos.CreateProdutoRequest;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.DTO.Produtos.ProdutoResponse;

public interface IRegistrarProduto {
    ProdutoResponse create(CreateProdutoRequest produtos);
}
