package com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.UseCase.Pedidos.GetPedidosByComprador;

import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.DTO.Pedidos.PedidoResponse;

import java.util.List;

public interface IGetPedidosByComprador {
    List<PedidoResponse> getPedidosByComprador();
}
