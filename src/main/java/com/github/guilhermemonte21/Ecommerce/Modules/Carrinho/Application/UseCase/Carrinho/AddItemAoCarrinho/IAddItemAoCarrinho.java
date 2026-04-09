package com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Application.UseCase.Carrinho.AddItemAoCarrinho;

import com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Application.DTO.Carrinho.CarrinhoResponse;

import java.util.UUID;

public interface IAddItemAoCarrinho {
    CarrinhoResponse adicionarAoCarrinho(UUID id, UUID idProduto, Long quantidade);
}
