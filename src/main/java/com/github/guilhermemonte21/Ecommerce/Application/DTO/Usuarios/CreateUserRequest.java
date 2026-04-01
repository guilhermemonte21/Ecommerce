package com.github.guilhermemonte21.Ecommerce.Application.DTO.Usuarios;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
        @NotBlank String nome,
        @NotBlank @Email(message = "Formato de email inválido") String email,
        @NotBlank @Size(min = 11, max = 11, message = "CPF deve ter 11 dígitos") String cpf,
        @NotBlank @Size(min = 8, message = "Senha deve ter no mínimo 8 caracteres") String senha) {
}
