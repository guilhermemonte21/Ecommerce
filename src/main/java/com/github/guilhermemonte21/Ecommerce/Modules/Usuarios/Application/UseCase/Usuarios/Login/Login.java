package com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.UseCase.Usuarios.Login;

import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.Gateway.UsuarioGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Domain.Entity.Usuarios;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

public class Login implements ILogin {

    private final UsuarioGateway gateway;
    private final PasswordEncoder encoder;

    public Login(UsuarioGateway gateway, PasswordEncoder encoder) {
        this.gateway = gateway;
        this.encoder = encoder;
    }

    @Override
    public Boolean login(String email, String senha) {
        Optional<Usuarios> optUsuario = gateway.findByEmail(email);
        if (optUsuario.isEmpty()) {
            return false;
        }
        Usuarios usuario = optUsuario.get();
        if (Boolean.FALSE.equals(usuario.getAtivo())) {
            return false;
        }
        return encoder.matches(senha, usuario.getSenha());
    }
}
