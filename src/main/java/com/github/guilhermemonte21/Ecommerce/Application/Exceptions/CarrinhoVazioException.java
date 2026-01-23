package com.github.guilhermemonte21.Ecommerce.Application.Exceptions;

public class CarrinhoVazioException extends RuntimeException {
    public CarrinhoVazioException() {
        super("Carrinho está vazio");
    }
}
