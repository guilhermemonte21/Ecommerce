package com.github.guilhermemonte21.Ecommerce.Application.Interface;

import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Usuarios;

import java.util.UUID;

public interface GerarToken {

        String gerarToken(Usuarios usuario);
        UUID extrairUsuarioId(String token);

}
