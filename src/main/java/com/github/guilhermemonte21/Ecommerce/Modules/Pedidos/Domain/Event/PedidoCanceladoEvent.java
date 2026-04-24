package com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Domain.Event;


@Deprecated
public class PedidoCanceladoEvent extends com.github.guilhermemonte21.Ecommerce.Shared.Domain.Event.PedidoCanceladoEvent {
    public PedidoCanceladoEvent(java.util.UUID pedidoId, String motivo, java.util.Map<java.util.UUID, Long> produtosParaRollback) {
        super(pedidoId, motivo, produtosParaRollback);
    }
}
