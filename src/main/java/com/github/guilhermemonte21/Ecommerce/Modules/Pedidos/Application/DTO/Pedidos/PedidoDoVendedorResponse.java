package com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.DTO.Pedidos;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record PedidoDoVendedorResponse(
        UUID id,
        UUID vendedorId,
        UUID produtoId,
        String nomeProduto,
        BigDecimal precoUnitario,
        Long quantidade,
        BigDecimal valorTotal,
        String status
) {}
