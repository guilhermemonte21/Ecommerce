package com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions;

public class UsuarioJaExisteException extends RuntimeException {
    public UsuarioJaExisteException(String email) {
        super("Usuário já existe com o email: " + email);
    }
}
