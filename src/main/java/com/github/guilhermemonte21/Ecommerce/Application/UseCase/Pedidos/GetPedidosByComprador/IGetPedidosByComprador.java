package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Pedidos.GetPedidosByComprador;

import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Pedidos;

import java.util.List;
import java.util.UUID;

public interface IGetPedidosByComprador {
    List<Pedidos> getPedidosByComprador(UUID IdComprador);
}
