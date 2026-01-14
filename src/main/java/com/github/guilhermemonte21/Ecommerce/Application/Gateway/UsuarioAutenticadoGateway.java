package com.github.guilhermemonte21.Ecommerce.Application.Gateway;

import com.github.guilhermemonte21.Ecommerce.Domain.Entity.UsuarioAutenticado;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Usuarios;

import java.util.UUID;

public interface UsuarioAutenticadoGateway {
    UsuarioAutenticado get();
}