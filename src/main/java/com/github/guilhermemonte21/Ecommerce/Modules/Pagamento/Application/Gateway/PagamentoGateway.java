package com.github.guilhermemonte21.Ecommerce.Modules.Pagamento.Application.Gateway;

import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Domain.Entity.Pedidos;

public interface PagamentoGateway {
    boolean processarPagamento(Pedidos pedido);
}
