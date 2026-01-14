package com.github.guilhermemonte21.Ecommerce.Application.Exceptions;

public class UsuarioInativoException extends RuntimeException {
    public UsuarioInativoException() {

        super("Operação indisponivel pois cliente esta inativo");
    }
}
