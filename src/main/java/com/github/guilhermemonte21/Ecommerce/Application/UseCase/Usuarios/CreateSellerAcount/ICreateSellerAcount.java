package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Usuarios.CreateSellerAcount;

import com.github.guilhermemonte21.Ecommerce.API.DTO.LoginRequest;
import com.github.guilhermemonte21.Ecommerce.Application.DTO.Usuarios.UsuarioResponse;

import java.util.UUID;

public interface ICreateSellerAcount {
    UsuarioResponse create(LoginRequest log, String stripeAccountId);
}
