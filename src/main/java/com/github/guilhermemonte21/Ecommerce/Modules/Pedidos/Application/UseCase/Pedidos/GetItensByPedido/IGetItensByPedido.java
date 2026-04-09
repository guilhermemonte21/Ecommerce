package com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.UseCase.Pedidos.GetItensByPedido;

import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.DTO.Pedidos.PedidoDoVendedorResponse;

import java.util.List;
import java.util.UUID;

public interface IGetItensByPedido {
    List<PedidoDoVendedorResponse> get(UUID idPedido);
}
