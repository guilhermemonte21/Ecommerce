package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Carrinho.GetCarrinhoById;

import com.github.guilhermemonte21.Ecommerce.Application.DTO.Carrinho.CarrinhoResponse;

import java.util.UUID;

public interface IGetCarrinhoById {
    CarrinhoResponse FindCarrinhoById(UUID Id);
}
