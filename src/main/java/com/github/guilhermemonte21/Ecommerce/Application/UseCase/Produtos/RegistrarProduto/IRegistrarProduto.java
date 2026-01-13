package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Produtos.RegistrarProduto;

import com.github.guilhermemonte21.Ecommerce.Application.DTO.Produtos.CreateProdutoRequest;
import com.github.guilhermemonte21.Ecommerce.Application.DTO.Produtos.ProdutoResponse;

public interface IRegistrarProduto {
    ProdutoResponse Create(CreateProdutoRequest produtos);
}
