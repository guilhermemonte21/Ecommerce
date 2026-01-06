package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Usuarios.CreateUser;

import com.github.guilhermemonte21.Ecommerce.Application.Gateway.UsuarioGateway;
import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Usuarios;
import org.springframework.stereotype.Service;

@Service
public class CreateUser implements ICreateUser{
    private final UsuarioGateway gateway;

    public CreateUser(UsuarioGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public Usuarios CreateUser(Usuarios newUser){
        return gateway.salvar(newUser);

    }
}
