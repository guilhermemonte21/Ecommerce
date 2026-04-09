package com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.UseCase.Usuarios.GetUserById;

import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.DTO.Usuarios.UsuarioResponse;

import java.util.UUID;

public interface IGetUserById {
    UsuarioResponse getUser(UUID id);
}
