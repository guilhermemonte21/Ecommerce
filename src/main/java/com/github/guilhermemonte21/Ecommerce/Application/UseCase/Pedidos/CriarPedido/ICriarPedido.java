package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Pedidos.CriarPedido;

import com.github.guilhermemonte21.Ecommerce.Application.DTO.Pedidos.PedidoResponse;

public interface ICriarPedido {
    PedidoResponse criarPedido(String endereco);
}
