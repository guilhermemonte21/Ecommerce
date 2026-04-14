package com.github.guilhermemonte21.Ecommerce.Shared.Infra.Event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Port.EventPublisher;
import com.github.guilhermemonte21.Ecommerce.Shared.Domain.Event.DomainEvent;
import com.github.guilhermemonte21.Ecommerce.Shared.Infra.Persistence.Entity.Data.OutboxEventEntity;
import com.github.guilhermemonte21.Ecommerce.Shared.Infra.Persistence.JpaRepository.JpaOutboxEventRepository;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQEventPublisher implements EventPublisher {

    private static final Logger log = LoggerFactory.getLogger(RabbitMQEventPublisher.class);

    private final JpaOutboxEventRepository outboxRepository;
    private final ObjectMapper objectMapper;
    private final Tracer tracer;

    public RabbitMQEventPublisher(JpaOutboxEventRepository outboxRepository,
            ObjectMapper objectMapper,
            Tracer tracer) {
        this.outboxRepository = outboxRepository;
        this.objectMapper = objectMapper;
        this.tracer = tracer;
    }

    @Override
    public void publish(DomainEvent event) {
        try {
            String payload = objectMapper.writeValueAsString(event);

            OutboxEventEntity outboxEvent = new OutboxEventEntity();
            outboxEvent.setEventType(event.eventType());
            outboxEvent.setPayload(payload);
            outboxEvent.setOccurredOn(event.occurredOn());
            outboxEvent.setProcessed(false);

            Span currentSpan = tracer.currentSpan();
            if (currentSpan != null) {
                outboxEvent.setTraceId(currentSpan.context().traceId());
                outboxEvent.setSpanId(currentSpan.context().spanId());
            }

            outboxRepository.save(outboxEvent);
            log.info("Evento '{}' gravado na tabela Outbox para processamento assíncrono.", event.eventType());
        } catch (JsonProcessingException e) {
            log.error("Erro fatal ao serializar evento '{}' para o Outbox", event.eventType(), e);
            throw new RuntimeException("Falha ao preparar evento para mensageria", e);
        }
    }
}
