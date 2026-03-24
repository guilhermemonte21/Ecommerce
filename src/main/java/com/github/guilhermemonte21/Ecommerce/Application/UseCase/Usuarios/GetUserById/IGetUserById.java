package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Usuarios.GetUserById;

import com.github.guilhermemonte21.Ecommerce.Application.DTO.Usuarios.UsuarioResponse;

import java.util.UUID;

public interface IGetUserById {
    UsuarioResponse getUser(UUID id);
}
