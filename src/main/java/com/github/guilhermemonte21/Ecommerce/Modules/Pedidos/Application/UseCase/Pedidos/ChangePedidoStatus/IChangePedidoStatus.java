package com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.UseCase.Pedidos.ChangePedidoStatus;

import java.util.UUID;

public interface IChangePedidoStatus {
    void mudarStatus(UUID idPedido);
}
