package com.github.guilhermemonte21.Ecommerce.Domain.Event;

import java.time.OffsetDateTime;
import java.util.UUID;

public class PagamentoConcluidoEvent implements DomainEvent {

    private final UUID pedidoId;
    private final OffsetDateTime occurredOn;

    public PagamentoConcluidoEvent(UUID pedidoId) {
        this.pedidoId = pedidoId;
        this.occurredOn = OffsetDateTime.now();
    }

    public UUID getPedidoId() { return pedidoId; }

    @Override
    public String eventType() { return "pagamento.concluido"; }

    @Override
    public OffsetDateTime occurredOn() { return occurredOn; }
}
