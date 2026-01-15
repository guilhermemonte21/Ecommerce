package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Usuarios.CreateSellerAcount;

import com.github.guilhermemonte21.Ecommerce.API.DTO.LoginRequest;
import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.AcessoNegadoException;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.UsuarioGateway;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Usuarios.Login.ILogin;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Usuarios;

import java.util.UUID;

public class CreateSellerAcount implements ICreateSellerAcount{
    private final ILogin login;
    private final UsuarioGateway gateway;
    public CreateSellerAcount(ILogin login, UsuarioGateway gateway) {
        this.login = login;
        this.gateway = gateway;
    }

    @Override
    public Usuarios create(LoginRequest log, UUID GatewayId) {
        Boolean logado = login.Login(log.email(), log.senha());
        if (!logado){
            throw new AcessoNegadoException();
        }
        Usuarios user = gateway.findByEmail(log.email());

        user.setTipoUsuario("Vendedor");
        user.setGatewayAccountId(GatewayId);

        Usuarios salvo = gateway.salvar(user);
        return salvo;
    }
}
