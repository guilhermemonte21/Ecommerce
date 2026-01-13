package com.github.guilhermemonte21.Ecommerce.Application.Exceptions;

import java.util.UUID;

public class ProdutoNotFoundException extends RuntimeException{
    public ProdutoNotFoundException(UUID Id){
        super("Produto com id: " + Id + " não encontrado");
    }
}
