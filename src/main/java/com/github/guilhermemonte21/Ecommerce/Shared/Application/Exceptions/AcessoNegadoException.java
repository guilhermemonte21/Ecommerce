package com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions;

public class AcessoNegadoException extends RuntimeException {
    public AcessoNegadoException() {
        super("Acesso Negado");
    }
}
