package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Carrinho.CriarCarrinho;

import com.github.guilhermemonte21.Ecommerce.Application.DTO.Carrinho.CreateCarrinhoDTO.CreateCarrinhoRequest;
import com.github.guilhermemonte21.Ecommerce.Application.DTO.Carrinho.CarrinhoResponse;

public interface ICriarCarrinho {
    CarrinhoResponse Criar(CreateCarrinhoRequest carrinho);
}
