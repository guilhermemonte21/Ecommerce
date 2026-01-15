package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Usuarios.DesativarConta;

import com.github.guilhermemonte21.Ecommerce.API.DTO.LoginRequest;
import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.AcessoNegadoException;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.UsuarioGateway;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Usuarios.Login.ILogin;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Usuarios;
import org.springframework.stereotype.Service;

@Service
public class MudarAtividadeDaConta implements IMudarAtividadeDaConta{
    private final UsuarioGateway gateway;
    private final ILogin login;

    public MudarAtividadeDaConta(UsuarioGateway gateway, ILogin login) {
        this.gateway = gateway;
        this.login = login;
    }

    @Override
    public Boolean MudarAtividadeDaConta(LoginRequest login1) {
        Boolean log = login.Login(login1.email(), login1.senha());
        if (!log){
            throw new AcessoNegadoException();
        }
        Usuarios user = gateway.findByEmail(login1.email());
        if (user.getAtivo() == true){
        user.setAtivo(false);
        gateway.salvar(user);
        }
        if (user.getAtivo() == false) {
            user.setAtivo(true);
            gateway.salvar(user);
        }
        return user.getAtivo();
        
    }
}
