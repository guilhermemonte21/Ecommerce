package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Pedidos.ChangePedidoStatus;

import java.util.UUID;

public interface IChangePedidoStatus {
    void ChangePedidosStatus(UUID IdPedido);
}
