package com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.UseCase.Usuarios.GetUserById;

import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.DTO.Usuarios.UsuarioResponse;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions.AcessoNegadoException;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions.UsuarioNotFoundException;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.Gateway.UsuarioAutenticadoGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.Gateway.UsuarioGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.Mappers.UsuarioMapperApl;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Domain.Entity.UsuarioAutenticado;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Domain.Entity.Usuarios;

import java.util.UUID;

public class GetUserById implements IGetUserById {

    private final UsuarioGateway gateway;
    private final UsuarioMapperApl mapperApl;
    private final UsuarioAutenticadoGateway authGateway;

    public GetUserById(UsuarioGateway gateway, UsuarioMapperApl mapperApl, UsuarioAutenticadoGateway authGateway) {
        this.gateway = gateway;
        this.mapperApl = mapperApl;
        this.authGateway = authGateway;
    }

    @Override
    public UsuarioResponse getUser(UUID id) {
        UsuarioAutenticado caller = authGateway.get();
        if (!caller.getUser().getId().equals(id)) {
            throw new AcessoNegadoException();
        }
        Usuarios user = gateway.getById(id).orElseThrow(() -> new UsuarioNotFoundException(id));
        return mapperApl.toResponse(user);
    }
}
