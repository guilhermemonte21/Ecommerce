package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Usuarios.Login;

import com.github.guilhermemonte21.Ecommerce.Application.Gateway.UsuarioGateway;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Usuarios;
import org.springframework.security.crypto.password.PasswordEncoder;

public class Login implements ILogin {

    private final UsuarioGateway gateway;
    private final PasswordEncoder encoder;

    public Login(UsuarioGateway gateway, PasswordEncoder encoder) {
        this.gateway = gateway;
        this.encoder = encoder;
    }

    @Override
    public Boolean login(String email, String senha) {
        Usuarios usuario = gateway.findByEmail(email);
        if (usuario == null) {
            return false;
        }
        return encoder.matches(senha, usuario.getSenha());
    }
}
