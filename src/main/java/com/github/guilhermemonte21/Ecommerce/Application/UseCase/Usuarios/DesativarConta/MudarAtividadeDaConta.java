package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Usuarios.DesativarConta;

import com.github.guilhermemonte21.Ecommerce.API.DTO.LoginRequest;
import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.AcessoNegadoException;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.UsuarioGateway;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Usuarios.Login.ILogin;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Usuarios;

public class MudarAtividadeDaConta implements IMudarAtividadeDaConta{
    private final UsuarioGateway gateway;
    private final ILogin login;

    public MudarAtividadeDaConta(UsuarioGateway gateway, ILogin login) {
        this.gateway = gateway;
        this.login = login;
    }

    @Override
    public Boolean mudarAtividade(LoginRequest login1) {
        Boolean autenticado = login.login(login1.email(), login1.senha());
        if (!autenticado){
            throw new AcessoNegadoException();
        }
        Usuarios user = gateway.findByEmail(login1.email());
        user.setAtivo(!user.getAtivo());
        gateway.salvar(user);
        return user.getAtivo();
        
    }
}
