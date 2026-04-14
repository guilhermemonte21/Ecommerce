package com.github.guilhermemonte21.Ecommerce.Shared.Infra.Jobs;

import com.github.guilhermemonte21.Ecommerce.Shared.Infra.Config.RabbitMQConfig;
import com.github.guilhermemonte21.Ecommerce.Shared.Infra.Persistence.Entity.Data.OutboxEventEntity;
import com.github.guilhermemonte21.Ecommerce.Shared.Infra.Persistence.JpaRepository.JpaOutboxEventRepository;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Component
public class OutboxRelayJob {

    private static final Logger log = LoggerFactory.getLogger(OutboxRelayJob.class);

    private final JpaOutboxEventRepository repository;
    private final RabbitTemplate rabbitTemplate;
    private final Tracer tracer;

    public OutboxRelayJob(JpaOutboxEventRepository repository,
            RabbitTemplate rabbitTemplate,
            Tracer tracer) {
        this.repository = repository;
        this.rabbitTemplate = rabbitTemplate;
        this.tracer = tracer;
    }

    @Scheduled(fixedDelayString = "3000")
    @Transactional
    public void processOutbox() {
        List<OutboxEventEntity> pendingEvents = repository.findUnprocessedEvents(PageRequest.of(0, 50));

        if (pendingEvents.isEmpty()) {
            return;
        }

        log.info("Processando lote de {} evento(s) do Outbox...", pendingEvents.size());

        for (OutboxEventEntity entity : pendingEvents) {
            Span relaySpan = tracer.nextSpan().name("outbox-relay").start();

            if (entity.getTraceId() != null) {
                relaySpan.tag("outbox.original.traceId", entity.getTraceId());
            }
            if (entity.getSpanId() != null) {
                relaySpan.tag("outbox.original.spanId", entity.getSpanId());
            }
            relaySpan.tag("outbox.eventType", entity.getEventType());

            try (Tracer.SpanInScope ws = tracer.withSpan(relaySpan)) {
                rabbitTemplate.convertAndSend(
                        RabbitMQConfig.EXCHANGE_EVENTS,
                        entity.getEventType(),
                        entity.getPayload());

                entity.setProcessed(true);
                entity.setProcessedAt(OffsetDateTime.now());
                repository.save(entity);

                log.debug("Evento '{}' (ID: {}) enviado. Outbox atualizado.", entity.getEventType(), entity.getId());

            } catch (Exception e) {
                relaySpan.error(e);
                log.error("Erro inesperado ao enviar evento ID {} - será tentado novamente.", entity.getId(), e);
                entity.setErrorMessage(e.getMessage());
                repository.save(entity);
            } finally {
                relaySpan.end();
            }
        }
    }
}
