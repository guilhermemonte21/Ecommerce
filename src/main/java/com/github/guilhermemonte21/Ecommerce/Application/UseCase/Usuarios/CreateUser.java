package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Usuarios;

import com.github.guilhermemonte21.Ecommerce.Application.Gateway.UsuarioGateway;
import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Usuarios;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Service
public class CreateUser {
    private final UsuarioGateway gateway;

    public CreateUser(UsuarioGateway gateway) {
        this.gateway = gateway;
    }

    public Usuarios CreateUser(Usuarios newUser){
        return gateway.salvar(newUser);

    }
}
