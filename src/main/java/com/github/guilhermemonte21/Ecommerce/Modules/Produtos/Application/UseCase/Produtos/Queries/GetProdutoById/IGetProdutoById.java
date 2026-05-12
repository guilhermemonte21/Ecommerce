package com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.UseCase.Produtos.Queries.GetProdutoById;

import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.DTO.Produtos.ProdutoResponse;

import java.util.UUID;

public interface IGetProdutoById {
   // @Cacheable(value = CacheNames.PRODUTO, key = "#id", sync = true)
    ProdutoResponse getProdutoById(UUID id);
}
