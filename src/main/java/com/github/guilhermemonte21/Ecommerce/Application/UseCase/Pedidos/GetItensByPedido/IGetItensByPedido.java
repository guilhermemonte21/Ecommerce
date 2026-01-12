package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Pedidos.GetItensByPedido;

import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.PedidoDoVendedor;

import java.util.List;
import java.util.UUID;

public interface IGetItensByPedido {
    List<PedidoDoVendedor> get(UUID IdPedido);
}
