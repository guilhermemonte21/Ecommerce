package com.github.guilhermemonte21.Ecommerce.Application.Gateway;

import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Usuarios;

import java.util.UUID;

public interface UsuarioGateway {

    Usuarios salvar(Usuarios user);

    Usuarios getById(UUID id);

}
