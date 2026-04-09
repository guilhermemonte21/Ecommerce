package com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions;

public class UsuarioInativoException extends RuntimeException {
    public UsuarioInativoException() {

        super("Operação indisponivel pois cliente esta inativo");
    }
}
