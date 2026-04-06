package com.github.guilhermemonte21.Ecommerce.Domain.Event;

import java.time.OffsetDateTime;
import java.util.UUID;

public class PedidoCanceladoEvent implements DomainEvent {

    private final UUID pedidoId;
    private final String motivo;
    private final OffsetDateTime occurredOn;

    public PedidoCanceladoEvent(UUID pedidoId, String motivo) {
        this.pedidoId = pedidoId;
        this.motivo = motivo;
        this.occurredOn = OffsetDateTime.now();
    }

    public UUID getPedidoId() { return pedidoId; }
    public String getMotivo() { return motivo; }

    @Override
    public String eventType() { return "pedido.cancelado"; }

    @Override
    public OffsetDateTime occurredOn() { return occurredOn; }
}
