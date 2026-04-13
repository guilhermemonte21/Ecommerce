package com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Domain.Entity;



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
    private UUID vendedorId;
    private String descricao;
    private BigDecimal preco;
    private Long estoque;
    private Long version;

    public Produtos(UUID id, String nomeProduto, String descricao, BigDecimal preco, Long estoque) {
        this.id = id;
        this.nomeProduto = nomeProduto;
        this.descricao = descricao;
        this.preco = preco;
        this.estoque = estoque;
    }

    public Produtos(UUID id, String nomeProduto, String descricao, BigDecimal preco, Long estoque, Long version) {
        this.id = id;
        this.nomeProduto = nomeProduto;
        this.descricao = descricao;
        this.preco = preco;
        this.estoque = estoque;
        this.version = version;
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
