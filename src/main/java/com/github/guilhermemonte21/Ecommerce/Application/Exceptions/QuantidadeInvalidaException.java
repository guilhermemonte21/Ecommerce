package com.github.guilhermemonte21.Ecommerce.Application.Exceptions;

public class QuantidadeInvalidaException extends RuntimeException{
    public QuantidadeInvalidaException() {
        super("Quantidade Inválida");
    }
}
