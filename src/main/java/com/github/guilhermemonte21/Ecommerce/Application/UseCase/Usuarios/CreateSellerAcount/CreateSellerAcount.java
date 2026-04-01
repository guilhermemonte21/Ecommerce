package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Usuarios.CreateSellerAcount;

import com.github.guilhermemonte21.Ecommerce.API.DTO.LoginRequest;
import com.github.guilhermemonte21.Ecommerce.Application.DTO.Usuarios.UsuarioResponse;
import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.AcessoNegadoException;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.UsuarioGateway;
import com.github.guilhermemonte21.Ecommerce.Application.Mappers.UsuarioMapperApl;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Usuarios.Login.ILogin;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Usuarios;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateSellerAcount implements ICreateSellerAcount {

    private static final Logger log = LoggerFactory.getLogger(CreateSellerAcount.class);

    private final ILogin login;
    private final UsuarioGateway gateway;
    private final UsuarioMapperApl mapperApl;

    public CreateSellerAcount(ILogin login, UsuarioGateway gateway, UsuarioMapperApl mapperApl) {
        this.login = login;
        this.gateway = gateway;
        this.mapperApl = mapperApl;
    }

    @Override
    public UsuarioResponse create(LoginRequest loginRequest, String stripeAccountId) {
        Boolean autenticado = login.login(loginRequest.email(), loginRequest.senha());
        if (!autenticado) {
            throw new AcessoNegadoException();
        }
        Usuarios user = gateway.findByEmail(loginRequest.email());
        user.setTipoUsuario("Vendedor");
        user.setStripeAccountId(stripeAccountId);

        Usuarios salvo = gateway.salvar(user);
        log.info("Conta de vendedor criada: id={}, email={}", salvo.getId(), salvo.getEmail());
        return mapperApl.toResponse(salvo);
    }
}
