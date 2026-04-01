package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Pedidos.GetPedidosByComprador;

import com.github.guilhermemonte21.Ecommerce.Application.DTO.Pedidos.PedidoResponse;

import java.util.List;
import java.util.UUID;

public interface IGetPedidosByComprador {
    List<PedidoResponse> getPedidosByComprador(UUID idComprador);
}
