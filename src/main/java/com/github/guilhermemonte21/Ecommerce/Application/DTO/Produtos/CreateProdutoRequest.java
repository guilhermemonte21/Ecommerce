package com.github.guilhermemonte21.Ecommerce.Application.DTO.Produtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CreateProdutoRequest(

        @NotBlank String nomeProduto,

        @NotBlank String descricao,
        @NotNull @Positive(message = "Preço deve ser maior que zero") BigDecimal preco,
        @NotNull @Positive(message = "Valor de estoque inválido") Long estoque) {
}
