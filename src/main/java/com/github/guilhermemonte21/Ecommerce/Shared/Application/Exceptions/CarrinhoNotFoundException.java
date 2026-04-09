package com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions;

import java.util.UUID;

public class CarrinhoNotFoundException extends RuntimeException{
    public CarrinhoNotFoundException(UUID Id){
        super("Carrinho com id: " + Id + " não encontrado");
    }
}
