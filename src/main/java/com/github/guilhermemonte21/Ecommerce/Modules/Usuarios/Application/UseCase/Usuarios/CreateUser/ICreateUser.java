package com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.UseCase.Usuarios.CreateUser;

import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.DTO.Usuarios.CreateUserRequest;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.DTO.Usuarios.UsuarioResponse;

public interface ICreateUser {
    UsuarioResponse createUser(CreateUserRequest newUser);
}
