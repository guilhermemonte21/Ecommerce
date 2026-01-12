package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Usuarios.GetUserById;

import com.github.guilhermemonte21.Ecommerce.Application.Gateway.UsuarioGateway;
import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Usuarios;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GetUserById implements IGetUserById{

    private final UsuarioGateway gateway;

    public GetUserById(UsuarioGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public Usuarios getUser(UUID Id) {
       return gateway.getById(Id).orElseThrow(() -> new RuntimeException("Usuario não encontrado"));
    }
}
