package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Pedidos.ChangePedidoStatus;

import com.github.guilhermemonte21.Ecommerce.Domain.Model.Enum.StatusPedido;

import java.util.UUID;

public interface IChangePedidoStatus {
    void ChangePedidosStatus(UUID IdPedido, StatusPedido status);
}
