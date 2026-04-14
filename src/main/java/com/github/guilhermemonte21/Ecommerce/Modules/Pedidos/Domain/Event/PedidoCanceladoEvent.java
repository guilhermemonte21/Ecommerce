package com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Domain.Event;

import com.github.guilhermemonte21.Ecommerce.Shared.Domain.Event.DomainEvent;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

public class PedidoCanceladoEvent implements DomainEvent {

    private final UUID pedidoId;
    private final String motivo;

    private final Map<UUID, Long> produtosParaRollback;
    private final OffsetDateTime occurredOn;

    public PedidoCanceladoEvent(UUID pedidoId, String motivo, Map<UUID, Long> produtosParaRollback) {
        this.pedidoId = pedidoId;
        this.motivo = motivo;
        this.produtosParaRollback = produtosParaRollback;
        this.occurredOn = OffsetDateTime.now();
    }

    public UUID getPedidoId() {
        return pedidoId;
    }

    public String getMotivo() {
        return motivo;
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
}
