package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Usuarios.CreateSellerAcount;

import com.github.guilhermemonte21.Ecommerce.Application.DTO.Usuarios.UsuarioResponse;

public interface ICreateSellerAcount {
    UsuarioResponse create(String stripeAccountId);
}
