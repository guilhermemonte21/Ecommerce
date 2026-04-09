package com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.Gateway;

import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Domain.Entity.PedidoDoVendedor;

public interface PedidoDoVendedorGateway {
    PedidoDoVendedor save(PedidoDoVendedor pedidoDoVendedor);
}
