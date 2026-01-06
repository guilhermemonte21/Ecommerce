package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Carrinho.CriarCarrinho;

import com.github.guilhermemonte21.Ecommerce.Application.DTO.Carrinho.CreateCarrinhoRequest;
import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Carrinho;

public interface ICriarCarrinho {
    Carrinho Criar(CreateCarrinhoRequest carrinho);
}
