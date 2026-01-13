package com.github.guilhermemonte21.Ecommerce.Application.DTO.Produtos;

import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Usuarios;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;


public record CreateProdutoRequest(

     UUID id,
     String nomeProduto,
     UUID vendedor,
     String descricao,
     BigDecimal preco,
    Long estoque
){}
