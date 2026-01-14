package com.github.guilhermemonte21.Ecommerce.Application.Exceptions;

import java.util.UUID;

public class AcessoNegadoException extends RuntimeException{
    public AcessoNegadoException(){
        super("Acesso Negado");
    }
}
