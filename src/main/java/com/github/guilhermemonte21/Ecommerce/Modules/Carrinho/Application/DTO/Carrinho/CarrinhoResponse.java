package com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Application.DTO.Carrinho;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import com.github.guilhermemonte21.Ecommerce.Modules.Carrinho.Domain.Entity.CarrinhoItem;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CarrinhoResponse {
    private UUID idCarrinho;
    private List<CarrinhoItem> itens;
    private UUID compradorId;
    private BigDecimal valorTotal;
    private OffsetDateTime atualizadoEm;
}
