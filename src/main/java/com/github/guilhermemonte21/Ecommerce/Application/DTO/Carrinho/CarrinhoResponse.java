package com.github.guilhermemonte21.Ecommerce.Application.DTO.Carrinho;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CarrinhoResponse {
    private UUID IdCarrinho;
    private List<String> Produtos;
    private String Comprador;
    private BigDecimal ValorTotal;
    private OffsetDateTime AtualizadoEm;
}
