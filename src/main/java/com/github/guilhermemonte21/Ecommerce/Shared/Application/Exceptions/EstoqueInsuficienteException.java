package com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions;

public class EstoqueInsuficienteException extends RuntimeException {
    public EstoqueInsuficienteException(String nomeProduto) {
        super("Estoque insuficiente para o produto: " + nomeProduto);
    }
}
