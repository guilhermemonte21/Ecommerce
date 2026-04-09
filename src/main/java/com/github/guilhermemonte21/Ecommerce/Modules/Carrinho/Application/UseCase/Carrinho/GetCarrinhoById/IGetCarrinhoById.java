package com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Application.UseCase.Carrinho.GetCarrinhoById;

import com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Application.DTO.Carrinho.CarrinhoResponse;

import java.util.UUID;

public interface IGetCarrinhoById {
    CarrinhoResponse findCarrinhoById(UUID id);
}
