package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Usuarios.CreateUser;

import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Usuarios;

public interface ICreateUser {
    Usuarios CreateUser(Usuarios newUser);
}
