package com.github.guilhermemonte21.Ecommerce.Application.DTO.Produtos;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
public class ProdutoResponse {
    private UUID IdProduto;
    private String NomeProduto;
    private String NomeVendedor;
    private BigDecimal Preco;
    private String Descricao;
    private Long Estoque;
}
