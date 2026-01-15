package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Usuarios.CreateSellerAcount;

import com.github.guilhermemonte21.Ecommerce.API.DTO.LoginRequest;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Usuarios;

import java.util.UUID;

public interface ICreateSellerAcount {
    Usuarios create(LoginRequest log, UUID GatewayId);
}
