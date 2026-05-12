package com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions;

public class ProdutoNaoPertenceAoVendedorException extends RuntimeException {
    public ProdutoNaoPertenceAoVendedorException(String nomeProduto) {
        super("Não é possível comprar o próprio produto: " + nomeProduto);
    }
}
