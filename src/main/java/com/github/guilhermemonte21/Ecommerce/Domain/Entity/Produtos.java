package com.github.guilhermemonte21.Ecommerce.Domain.Entity;

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
public class Produtos {
    private UUID id;
    private String nomeProduto;
    private Usuarios vendedor;
    private String descricao;
    private BigDecimal preco;
    private Long estoque;

    public Produtos(UUID id, String nomeProduto, String descricao, BigDecimal preco, Long estoque) {
        this.id = id;
        this.nomeProduto = nomeProduto;
        this.descricao = descricao;
        this.preco = preco;
        this.estoque = estoque;
    }

    public void atualizarEstoque(Long quantity) {
        if (quantity == null) {
            return;
        }
        if (this.estoque == null) {
            this.estoque = 0L;
        }
        this.estoque += quantity;
    }
}
