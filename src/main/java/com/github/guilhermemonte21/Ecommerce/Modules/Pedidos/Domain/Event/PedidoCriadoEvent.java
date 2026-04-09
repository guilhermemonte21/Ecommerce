package com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Domain.Event;

import com.github.guilhermemonte21.Ecommerce.Shared.Domain.Event.DomainEvent;

import java.time.OffsetDateTime;
import java.util.UUID;

public class PedidoCriadoEvent implements DomainEvent {

    private final UUID pedidoId;
    private final UUID compradorId;
    private final OffsetDateTime occurredOn;

    public PedidoCriadoEvent(UUID pedidoId, UUID compradorId) {
        this.pedidoId = pedidoId;
        this.compradorId = compradorId;
        this.occurredOn = OffsetDateTime.now();
    }

    public UUID getPedidoId() { return pedidoId; }
    public UUID getCompradorId() { return compradorId; }

    @Override
    public String eventType() { return "pedido.criado"; }

    @Override
    public OffsetDateTime occurredOn() { return occurredOn; }
}
