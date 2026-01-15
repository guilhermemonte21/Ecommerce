package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Usuarios.DesativarConta;

import com.github.guilhermemonte21.Ecommerce.API.DTO.LoginRequest;

public interface IMudarAtividadeDaConta {
    Boolean MudarAtividadeDaConta(LoginRequest login);
    }

