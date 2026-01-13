package com.github.guilhermemonte21.Ecommerce.Infra.Gateway.Impl;

import com.github.guilhermemonte21.Ecommerce.Application.Gateway.UsuarioAutenticadoGateway;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.UsuarioAutenticado;
import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.Entity.Data.UsuariosEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UsuarioAutenticadoImpl implements UsuarioAutenticadoGateway {

    @Override
    public UsuarioAutenticado get() {
        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        UUID IdUser = UUID.fromString(auth.getName());


        return new UsuarioAutenticado(
                IdUser

        );
    }}