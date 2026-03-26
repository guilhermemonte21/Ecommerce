package com.github.guilhermemonte21.Ecommerce.Application.DTO.Pedidos;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record PedidoResponse(
        UUID IdPedido,
        String NomeComprador,
        List<UUID> itens,
        String EndereçoDeEntrega,
        BigDecimal preco,
        OffsetDateTime criadoEm) {
}
