package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Usuarios.DesativarConta;

import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.AcessoNegadoException;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.UsuarioGateway;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Usuarios.Login.ILogin;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Usuarios;

public class DesativarConta implements IDesativarConta{
    private final UsuarioGateway gateway;
    private final ILogin login;

    public DesativarConta(UsuarioGateway gateway, ILogin login) {
        this.gateway = gateway;
        this.login = login;
    }

    @Override
    public Boolean DesativarConta(String Email, String Senha) {
        Boolean log = login.Login(Email, Senha);
        if (log){
            Usuarios user = gateway.findByEmail(Email);
            user.setAtivo(false);
            gateway.salvar(user);
            return user.getAtivo();
        }
        throw new AcessoNegadoException();
    }
}
