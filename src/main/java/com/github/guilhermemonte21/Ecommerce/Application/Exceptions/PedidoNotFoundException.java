package com.github.guilhermemonte21.Ecommerce.Application.Exceptions;

import java.util.UUID;

public class PedidoNotFoundException extends RuntimeException{
    public PedidoNotFoundException(UUID Id){
        super("Pedido com id: " + Id + " não encontrado");
    }
}
