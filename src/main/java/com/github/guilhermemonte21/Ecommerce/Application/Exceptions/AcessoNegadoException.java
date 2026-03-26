package com.github.guilhermemonte21.Ecommerce.Application.Exceptions;

public class AcessoNegadoException extends RuntimeException {
    public AcessoNegadoException() {
        super("Acesso Negado");
    }
}
