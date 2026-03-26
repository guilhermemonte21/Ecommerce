package com.github.guilhermemonte21.Ecommerce.Domain.Entity;

public class UsuarioAutenticado {
    private final Usuarios user;

    public UsuarioAutenticado(Usuarios user) {
        this.user = user;
    }

    public Usuarios getUser() {
        return user;
    }
}