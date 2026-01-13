package com.github.guilhermemonte21.Ecommerce.Domain.Entity;



import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
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
    private UUID Id;

    @NotNull
    private Usuarios Comprador;
    @Nullable
    private List<Produtos> Itens = new ArrayList<>();
    private BigDecimal ValorTotal;
    private OffsetDateTime AtualizadoEm;


    public void atualizarValorTotal() {
        this.ValorTotal = this.Itens.stream()
                .map(Produtos::getPreco)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

    }
    public void AtualizadoAgora(){
        this.AtualizadoEm = OffsetDateTime.now();
    }
    public void Limpar(){
        this.getItens().clear();
    }
}
