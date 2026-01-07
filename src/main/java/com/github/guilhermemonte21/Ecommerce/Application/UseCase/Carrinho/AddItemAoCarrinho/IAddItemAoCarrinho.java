package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Carrinho.AddItemAoCarrinho;

import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Carrinho;

import java.util.UUID;

public interface IAddItemAoCarrinho {
    Carrinho AdicionarAoCarrinho(UUID id, UUID IdProduto, Long quantidade);
}
