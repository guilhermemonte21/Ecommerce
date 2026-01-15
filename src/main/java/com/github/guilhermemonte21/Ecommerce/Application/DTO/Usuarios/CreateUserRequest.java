package com.github.guilhermemonte21.Ecommerce.Application.DTO.Usuarios;

import jakarta.validation.constraints.NotBlank;

public record CreateUserRequest(
        @NotBlank
        String nome,
        @NotBlank
        String email,
        @NotBlank

        String cpf,
        @NotBlank
        String senha) {}
