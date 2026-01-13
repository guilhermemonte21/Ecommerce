package com.github.guilhermemonte21.Ecommerce.Application.DTO.Produtos;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;


public record ProdutoResponse (
     UUID IdProduto,
     String NomeProduto,
     String NomeVendedor,
     BigDecimal Preco,
     String Descricao,
     Long Estoque
){}
