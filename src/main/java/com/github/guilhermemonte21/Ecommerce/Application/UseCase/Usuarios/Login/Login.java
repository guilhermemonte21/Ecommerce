package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Usuarios.Login;

import com.github.guilhermemonte21.Ecommerce.Application.Gateway.UsuarioAutenticadoGateway;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.UsuarioGateway;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.UsuarioAutenticado;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Usuarios;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class Login implements ILogin{

    private final UsuarioGateway gateway;
    private final PasswordEncoder encoder;

    public Login(UsuarioGateway gateway, PasswordEncoder encoder) {
        this.gateway = gateway;
        this.encoder = encoder;
    }

    @Override
    public Boolean Login(String Email, String Senha) {
        Usuarios usuario = gateway.findByEmail(Email);


            boolean senhaCorreta = encoder.matches(Senha, usuario.getSenha());
            //System.out.println("Senha correta?" + senhaCorreta); // log de teste senha
            return senhaCorreta;
    }
}
