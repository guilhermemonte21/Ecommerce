package com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Domain.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarrinhoItem {
    private UUID produtoId;
    private String nomeProduto;
    private BigDecimal preco;
    private Long quantidade;
}
