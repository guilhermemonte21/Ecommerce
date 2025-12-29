package com.github.guilhermemonte21.Ecommerce.Application.UseCase;

import com.github.guilhermemonte21.Ecommerce.Application.Gateway.UsuarioGateway;
import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Usuarios;
import org.springframework.stereotype.Service;

@Service
public class CreateUser {
    private final UsuarioGateway gateway;

    public CreateUser(UsuarioGateway gateway) {
        this.gateway = gateway;
    }

}
