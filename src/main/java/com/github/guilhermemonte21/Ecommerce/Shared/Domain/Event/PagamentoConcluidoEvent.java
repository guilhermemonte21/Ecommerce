package com.github.guilhermemonte21.Ecommerce.Shared.Domain.Event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.UUID;

public class PagamentoConcluidoEvent implements DomainEvent {

    private final UUID pedidoId;
    private final OffsetDateTime occurredOn;

    @JsonCreator
    public PagamentoConcluidoEvent(@JsonProperty("pedidoId") UUID pedidoId) {
        this.pedidoId = pedidoId;
        this.occurredOn = OffsetDateTime.now();
    }

    public UUID getPedidoId() {
        return pedidoId;
    }

    @Override
    public String eventType() {
        return "pagamento.concluido";
    }

    @Override
    public OffsetDateTime occurredOn() {
        return occurredOn;
    }
}
