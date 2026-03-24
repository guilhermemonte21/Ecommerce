package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Usuarios.GetUserById;

import com.github.guilhermemonte21.Ecommerce.Application.DTO.Usuarios.UsuarioResponse;
import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.UsuarioNotFoundException;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.UsuarioGateway;
import com.github.guilhermemonte21.Ecommerce.Application.Mappers.UsuarioMapperApl;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Usuarios;

import java.util.UUID;

public class GetUserById implements IGetUserById {

    private final UsuarioGateway gateway;
    private final UsuarioMapperApl mapperApl;

    public GetUserById(UsuarioGateway gateway, UsuarioMapperApl mapperApl) {
        this.gateway = gateway;
        this.mapperApl = mapperApl;
    }

    @Override
    public UsuarioResponse getUser(UUID id) {
        Usuarios user = gateway.getById(id).orElseThrow(() -> new UsuarioNotFoundException(id));
        return mapperApl.toResponse(user);
    }
}
