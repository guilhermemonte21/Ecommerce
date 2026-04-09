package com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.Gateway;

import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Domain.Entity.Usuarios;

import java.util.Optional;
import java.util.UUID;

public interface UsuarioGateway {

    Usuarios salvar(Usuarios user);

    Optional<Usuarios> getById(UUID id);

    Usuarios findByEmail(String Email);
}
