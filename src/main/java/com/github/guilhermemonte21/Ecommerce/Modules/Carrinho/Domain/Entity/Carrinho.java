package com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Domain.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Carrinho {
    private UUID id;
    private UUID compradorId;
    private List<CarrinhoItem> itens = new ArrayList<>();
    private BigDecimal valorTotal;
    private OffsetDateTime atualizadoEm;

    public void atualizadoAgora() {
        this.atualizadoEm = OffsetDateTime.now();
    }

    public void adicionarItem(UUID produtoId, String nome, Long quantidade, BigDecimal precoUnitario) {
        this.itens.stream()
            .filter(i -> i.getProdutoId().equals(produtoId))
            .findFirst()
            .ifPresentOrElse(
                item -> item.setQuantidade(item.getQuantidade() + quantidade),
                () -> this.itens.add(new CarrinhoItem(produtoId, nome, precoUnitario, quantidade))
            );

        if (this.valorTotal == null) {
            this.valorTotal = BigDecimal.ZERO;
        }
        BigDecimal precoAAdicionar = precoUnitario.multiply(BigDecimal.valueOf(quantidade));
        this.valorTotal = this.valorTotal.add(precoAAdicionar);
        this.atualizadoAgora();
    }

    public void removerItem(UUID produtoId) {
        this.itens.stream()
            .filter(i -> i.getProdutoId().equals(produtoId))
            .findFirst()
            .ifPresent(item -> {
                BigDecimal precoItem = item.getPreco();
                if (item.getQuantidade() > 1) {
                    item.setQuantidade(item.getQuantidade() - 1);
                } else {
                    this.itens.remove(item);
                }
                
                if (this.valorTotal != null) {
                    this.valorTotal = this.valorTotal.subtract(precoItem);
                    if (this.valorTotal.compareTo(BigDecimal.ZERO) < 0) {
                        this.valorTotal = BigDecimal.ZERO;
                    }
                }
                this.atualizadoAgora();
            });
    }

    public void limpar() {
        this.itens.clear();
        this.valorTotal = BigDecimal.ZERO;
        this.atualizadoAgora();
    }
}
