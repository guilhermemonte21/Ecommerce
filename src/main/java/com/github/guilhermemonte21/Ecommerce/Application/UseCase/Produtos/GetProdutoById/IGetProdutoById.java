package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Produtos.GetProdutoById;

import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Produtos;

import java.util.Optional;
import java.util.UUID;

public interface IGetProdutoById {
    Optional<Produtos> GetProdutoById(UUID id);
}
