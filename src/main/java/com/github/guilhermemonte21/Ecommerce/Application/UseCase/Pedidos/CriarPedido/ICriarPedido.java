package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Pedidos.CriarPedido;

import com.github.guilhermemonte21.Ecommerce.Domain.Model.Entity.Pedidos;

import java.util.UUID;

public interface ICriarPedido {

    Pedidos CriarPedido(UUID CarrinhoId);
}
