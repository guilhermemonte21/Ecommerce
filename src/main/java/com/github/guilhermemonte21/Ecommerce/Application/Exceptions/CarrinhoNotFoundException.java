package com.github.guilhermemonte21.Ecommerce.Application.Exceptions;

import java.util.UUID;

public class CarrinhoNotFoundException extends RuntimeException{
    public CarrinhoNotFoundException(UUID Id){
        super("Carrinho com id: " + Id + " não encontrado");
    }
}
