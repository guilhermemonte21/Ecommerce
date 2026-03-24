package com.github.guilhermemonte21.Ecommerce.Domain.Entity;

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
    private Usuarios comprador;
    private List<Produtos> itens = new ArrayList<>();
    private BigDecimal valorTotal;
    private OffsetDateTime atualizadoEm;

    public void atualizarValorTotal() {
        this.valorTotal = this.itens.stream()
                .map(Produtos::getPreco)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void atualizadoAgora() {
        this.atualizadoEm = OffsetDateTime.now();
    }

    public void limpar() {
        this.getItens().clear();
    }
}
