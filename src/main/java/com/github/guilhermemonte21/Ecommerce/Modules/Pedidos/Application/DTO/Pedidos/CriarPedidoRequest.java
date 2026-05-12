package com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.DTO.Pedidos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CriarPedidoRequest(

        @NotBlank(message = "O endereço de entrega é obrigatório e não pode estar em branco.")
        @Size(min = 5, max = 255, message = "O endereço deve ter entre 5 e 255 caracteres.")
        String endereco

) {}
