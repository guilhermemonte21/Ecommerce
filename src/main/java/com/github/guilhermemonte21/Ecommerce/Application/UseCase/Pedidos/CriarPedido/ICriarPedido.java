package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Pedidos.CriarPedido;

import com.github.guilhermemonte21.Ecommerce.Application.DTO.Pedidos.PedidoResponse;

import java.util.UUID;

public interface ICriarPedido {

    PedidoResponse CriarPedido(UUID CarrinhoId);
}
