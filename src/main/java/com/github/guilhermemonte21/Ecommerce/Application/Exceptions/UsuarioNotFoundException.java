package com.github.guilhermemonte21.Ecommerce.Application.Exceptions;

import java.util.UUID;

public class UsuarioNotFoundException extends RuntimeException{
    public UsuarioNotFoundException(UUID Id){
        super("Usuario com id: " + Id + " não encontrado");
    }
}
