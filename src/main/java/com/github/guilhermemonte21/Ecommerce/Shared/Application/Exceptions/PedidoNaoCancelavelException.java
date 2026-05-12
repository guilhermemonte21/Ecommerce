package com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions;

import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Domain.Enum.StatusPedido;
import java.util.UUID;

public class PedidoNaoCancelavelException extends RuntimeException {
    public PedidoNaoCancelavelException(UUID pedidoId, StatusPedido status) {
        super("Pedido " + pedidoId + " não pode ser cancelado pois está com status " + status);
    }
}
