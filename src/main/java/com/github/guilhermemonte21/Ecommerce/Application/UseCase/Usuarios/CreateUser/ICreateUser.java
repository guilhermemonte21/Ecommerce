package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Usuarios.CreateUser;

import com.github.guilhermemonte21.Ecommerce.Application.DTO.Usuarios.CreateUserRequest;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Usuarios;

public interface ICreateUser {
    Usuarios CreateUser(CreateUserRequest newUser);
}
