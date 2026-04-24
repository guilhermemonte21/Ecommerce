package com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Domain.Event;


@Deprecated
public class PedidoCriadoEvent extends com.github.guilhermemonte21.Ecommerce.Shared.Domain.Event.PedidoCriadoEvent {
    public PedidoCriadoEvent(java.util.UUID pedidoId, java.util.UUID compradorId) {
        super(pedidoId, compradorId);
    }
}
