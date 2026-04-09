package com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions;

public class QuantidadeInvalidaException extends RuntimeException{
    public QuantidadeInvalidaException() {
        super("Quantidade Inválida");
    }
}
