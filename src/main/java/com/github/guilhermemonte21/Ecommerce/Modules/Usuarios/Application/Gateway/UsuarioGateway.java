package com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.Gateway;

import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Domain.Entity.Usuarios;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsuarioGateway {

    Usuarios salvar(Usuarios user);

    Optional<Usuarios> getById(UUID id);

    List<Usuarios> findAllByIds(List<UUID> ids);

    Optional<Usuarios> findByEmail(String Email);
}
