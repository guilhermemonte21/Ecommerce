package com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.DTO.Usuarios;

import java.util.UUID;

public record UsuarioResponse(
        UUID id,
        String nome,
        String email,
        String cpf,
        Boolean ativo,
        String tipoUsuario
) {}
