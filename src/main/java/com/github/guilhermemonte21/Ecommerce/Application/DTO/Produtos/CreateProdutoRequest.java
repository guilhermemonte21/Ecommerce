package com.github.guilhermemonte21.Ecommerce.Application.DTO.Produtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;


public record CreateProdutoRequest(

     UUID id,
     @NotBlank
     String nomeProduto,

     @NotBlank
     String descricao,
     @NotNull
     BigDecimal preco,
    @NotNull
    @Positive
    Long estoque
){}
