package com.github.guilhermemonte21.Ecommerce.Shared.Domain.Event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.UUID;

public class PagamentoConcluidoEvent implements DomainEvent {

    private final UUID pedidoId;
    private final String nomeComprador;
    private final String emailComprador;
    private final OffsetDateTime occurredOn;

    @JsonCreator
    public PagamentoConcluidoEvent(
            @JsonProperty("pedidoId") UUID pedidoId,
            @JsonProperty("nomeComprador") String nomeComprador,
            @JsonProperty("emailComprador") String emailComprador) {
        this.pedidoId = pedidoId;
        this.nomeComprador = nomeComprador;
        this.emailComprador = emailComprador;
        this.occurredOn = OffsetDateTime.now();
    }

    public UUID getPedidoId() {
        return pedidoId;
    }

    public String getNomeComprador() {
        return nomeComprador;
    }

    public String getEmailComprador() {
        return emailComprador;
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
