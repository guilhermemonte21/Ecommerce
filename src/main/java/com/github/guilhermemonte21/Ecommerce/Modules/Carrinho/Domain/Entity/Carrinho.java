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
    private List<UUID> produtoIds = new ArrayList<>();
    private BigDecimal valorTotal;
    private OffsetDateTime atualizadoEm;

    public void atualizadoAgora() {
        this.atualizadoEm = OffsetDateTime.now();
    }

    public void adicionarItem(UUID produtoId, Long quantidade, BigDecimal precoUnitario) {
        for (int i = 0; i < quantidade; i++) {
            this.produtoIds.add(produtoId);
        }
        if (this.valorTotal == null) {
            this.valorTotal = BigDecimal.ZERO;
        }
        BigDecimal precoAAdicionar = precoUnitario.multiply(BigDecimal.valueOf(quantidade));
        this.valorTotal = this.valorTotal.add(precoAAdicionar);
        this.atualizadoAgora();
    }

    public void removerItem(UUID produtoId, BigDecimal precoUnitario) {
        if (this.produtoIds.remove(produtoId)) {
            this.valorTotal = this.valorTotal.subtract(precoUnitario);
            if (this.valorTotal.compareTo(BigDecimal.ZERO) < 0) {
                this.valorTotal = BigDecimal.ZERO;
            }
            this.atualizadoAgora();
        }
    }

    public void limpar() {
        this.getProdutoIds().clear();
        this.valorTotal = BigDecimal.ZERO;
        this.atualizadoAgora();
    }
}
