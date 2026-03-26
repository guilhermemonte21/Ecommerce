package com.github.guilhermemonte21.Ecommerce.Application.Exceptions;

public class OperacaoDuplicadaException extends RuntimeException {
    public OperacaoDuplicadaException(String message) {
        super(message);
    }
}
