package com.github.guilhermemonte21.Ecommerce.Application.DTO.Pedidos;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public record PedidoResponse (
    UUID IdPedido,
    String NomeComprador,
    List<UUID> itens ,
    BigDecimal preco,
    OffsetDateTime criadoEm
){}
