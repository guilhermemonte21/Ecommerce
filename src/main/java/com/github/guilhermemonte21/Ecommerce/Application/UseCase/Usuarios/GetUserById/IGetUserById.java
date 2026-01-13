package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Usuarios.GetUserById;

import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Usuarios;

import java.util.UUID;

public interface IGetUserById {
    Usuarios getUser(UUID Id);
}
