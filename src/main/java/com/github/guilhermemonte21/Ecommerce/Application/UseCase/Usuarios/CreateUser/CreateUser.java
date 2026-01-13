package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Usuarios.CreateUser;

import com.github.guilhermemonte21.Ecommerce.Application.Gateway.UsuarioGateway;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Usuarios;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CreateUser implements ICreateUser{
    private final UsuarioGateway gateway;
    private final PasswordEncoder encoder;

    public CreateUser(UsuarioGateway gateway, PasswordEncoder encoder) {
        this.gateway = gateway;
        this.encoder = encoder;
    }

    @Override
    public Usuarios CreateUser(Usuarios newUser){

        newUser.setSenha(encoder.encode(newUser.getSenha()));

        return gateway.salvar(newUser);

    }
}
