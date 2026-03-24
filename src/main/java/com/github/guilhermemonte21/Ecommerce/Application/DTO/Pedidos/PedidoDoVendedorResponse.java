package com.github.guilhermemonte21.Ecommerce.Application.DTO.Pedidos;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record PedidoDoVendedorResponse(
        UUID id,
        String nomeVendedor,
        List<String> produtos,
        BigDecimal valor,
        String status
) {}
