package com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.UseCase.Pedidos.CriarPedido;

import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.DTO.Pedidos.CriarPedidoRequest;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.DTO.Pedidos.PedidoResponse;

public interface ICriarPedido {
    PedidoResponse criarPedido(CriarPedidoRequest request);
}
