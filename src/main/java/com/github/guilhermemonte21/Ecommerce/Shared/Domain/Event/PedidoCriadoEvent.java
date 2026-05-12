package com.github.guilhermemonte21.Ecommerce.Shared.Domain.Event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.UUID;

public class PedidoCriadoEvent implements DomainEvent {

    private final UUID pedidoId;
    private final UUID compradorId;
    private final String nomeComprador;
    private final String emailComprador;
    private final java.math.BigDecimal valorTotal;
    private final OffsetDateTime occurredOn;

    @JsonCreator
    public PedidoCriadoEvent(
            @JsonProperty("pedidoId") UUID pedidoId,
            @JsonProperty("compradorId") UUID compradorId,
            @JsonProperty("nomeComprador") String nomeComprador,
            @JsonProperty("emailComprador") String emailComprador,
            @JsonProperty("valorTotal") java.math.BigDecimal valorTotal) {
        this.pedidoId = pedidoId;
        this.compradorId = compradorId;
        this.nomeComprador = nomeComprador;
        this.emailComprador = emailComprador;
        this.valorTotal = valorTotal;
        this.occurredOn = OffsetDateTime.now();
    }

    public UUID getPedidoId() {
        return pedidoId;
    }

    public UUID getCompradorId() {
        return compradorId;
    }

    public String getNomeComprador() {
        return nomeComprador;
    }

    public String getEmailComprador() {
        return emailComprador;
    }

    public java.math.BigDecimal getValorTotal() {
        return valorTotal;
    }

    @Override
    public String eventType() {
        return "pedido.criado";
    }

    @Override
    public OffsetDateTime occurredOn() {
        return occurredOn;
    }
}
