package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Produtos.GetProdutoById;

import com.github.guilhermemonte21.Ecommerce.Application.DTO.Produtos.ProdutoResponse;

import java.util.UUID;

public interface IGetProdutoById {
   ProdutoResponse GetProdutoById(UUID id);
}
