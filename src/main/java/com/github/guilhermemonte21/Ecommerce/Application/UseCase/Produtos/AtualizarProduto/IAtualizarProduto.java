package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Produtos.AtualizarProduto;

import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Produtos;

import java.util.UUID;

public interface IAtualizarProduto {
    Produtos Atualizar(UUID IdUser, UUID IdProduto, Produtos produtos);
}
