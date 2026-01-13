package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Carrinho.AddItemAoCarrinho;

import com.github.guilhermemonte21.Ecommerce.Application.DTO.Carrinho.CarrinhoResponse;

import java.util.UUID;

public interface IAddItemAoCarrinho {
    CarrinhoResponse AdicionarAoCarrinho(UUID id, UUID IdProduto, Long quantidade);
}
