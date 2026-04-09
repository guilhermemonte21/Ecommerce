package com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.UseCase.Usuarios.CreateSellerAcount;

import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.DTO.Usuarios.UsuarioResponse;

public interface ICreateSellerAcount {
    UsuarioResponse create(String stripeAccountId);
}
