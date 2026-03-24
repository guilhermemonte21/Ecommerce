package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Pedidos.GetItensByPedido;

import com.github.guilhermemonte21.Ecommerce.Application.DTO.Pedidos.PedidoDoVendedorResponse;

import java.util.List;
import java.util.UUID;

public interface IGetItensByPedido {
    List<PedidoDoVendedorResponse> get(UUID idPedido);
}
