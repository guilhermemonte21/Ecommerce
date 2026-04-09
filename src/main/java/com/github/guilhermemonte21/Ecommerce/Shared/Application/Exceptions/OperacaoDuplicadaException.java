package com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions;

public class OperacaoDuplicadaException extends RuntimeException {
    public OperacaoDuplicadaException(String message) {
        super(message);
    }
}
