package com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity;



import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.Entity.Data.ProdutosEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Carrinho {
    private UUID Id;
    private Usuarios Comprador;
    private List<Produtos> Itens = new ArrayList<>();
    private BigDecimal ValorTotal;
    private OffsetDateTime AtualizadoEm;


    public void atualizarValorTotal() {
        this.ValorTotal = this.Itens.stream()
                .map(Produtos::getPreco)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

    }
}
