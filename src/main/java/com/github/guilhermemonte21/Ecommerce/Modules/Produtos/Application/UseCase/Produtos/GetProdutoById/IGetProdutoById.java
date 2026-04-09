package com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.UseCase.Produtos.GetProdutoById;

import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.DTO.Produtos.ProdutoResponse;

import java.util.UUID;

public interface IGetProdutoById {
    ProdutoResponse getProdutoById(UUID id);
}
