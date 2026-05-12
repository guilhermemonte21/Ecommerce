package com.github.guilhermemonte21.Ecommerce.Shared.Domain.Event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

public class PedidoCanceladoEvent implements DomainEvent {

    private final UUID pedidoId;
    private final String motivo;
    private final String nomeComprador;
    private final String emailComprador;
    private final Map<UUID, Long> produtosParaRollback;
    private final OffsetDateTime occurredOn;

    @JsonCreator
    public PedidoCanceladoEvent(
            @JsonProperty("pedidoId") UUID pedidoId,
            @JsonProperty("motivo") String motivo,
            @JsonProperty("nomeComprador") String nomeComprador,
            @JsonProperty("emailComprador") String emailComprador,
            @JsonProperty("produtosParaRollback") Map<UUID, Long> produtosParaRollback) {
        this.pedidoId = pedidoId;
        this.motivo = motivo;
        this.nomeComprador = nomeComprador;
        this.emailComprador = emailComprador;
        this.produtosParaRollback = produtosParaRollback;
        this.occurredOn = OffsetDateTime.now();
    }

    public UUID getPedidoId() {
        return pedidoId;
    }

    public String getMotivo() {
        return motivo;
    }

    public String getNomeComprador() {
        return nomeComprador;
    }

    public String getEmailComprador() {
        return emailComprador;
    }

    public Map<UUID, Long> getProdutosParaRollback() {
        return produtosParaRollback;
    }

    @Override
    public String eventType() {
        return "pedido.cancelado";
    }

    @Override
    public OffsetDateTime occurredOn() {
        return occurredOn;
    }

    public static PedidoCanceladoEvent por(UUID pedidoId, 
                                           com.github.guilhermemonte21.Ecommerce.Shared.Domain.Enum.MotivoCancelamentoPedido motivo,
                                           String nomeComprador, String emailComprador,
                                           Map<UUID, Long> produtos) {
        return new PedidoCanceladoEvent(
            pedidoId, motivo.getDescricao(), nomeComprador, emailComprador, produtos
        );
    }
}
