package com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity;



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
    private Usuarios Comprador;
    private List<Produtos> Itens = new ArrayList<>();
    private BigDecimal ValorTotal;
    private OffsetDateTime AtualizadoEm;


}
