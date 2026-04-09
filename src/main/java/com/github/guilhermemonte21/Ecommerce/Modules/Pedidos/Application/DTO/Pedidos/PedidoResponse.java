package com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.DTO.Pedidos;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record PedidoResponse(
        UUID idPedido,
        String nomeComprador,
        List<UUID> itens,
        String enderecoDeEntrega,
        BigDecimal preco,
        String status,
        OffsetDateTime criadoEm) {
}
