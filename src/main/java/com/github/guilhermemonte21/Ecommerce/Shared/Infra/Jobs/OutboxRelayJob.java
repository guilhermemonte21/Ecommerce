package com.github.guilhermemonte21.Ecommerce.Shared.Infra.Jobs;

import com.github.guilhermemonte21.Ecommerce.Shared.Infra.Config.RabbitMQConfig;
import com.github.guilhermemonte21.Ecommerce.Shared.Infra.Persistence.Entity.Data.OutboxEventEntity;
import com.github.guilhermemonte21.Ecommerce.Shared.Infra.Persistence.JpaRepository.JpaOutboxEventRepository;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${app.outbox.relay.batch-size:50}")
    private int batchSize;

    @Value("${app.outbox.relay.max-retries:5}")
    private int maxRetries;

    public OutboxRelayJob(JpaOutboxEventRepository repository,
            RabbitTemplate rabbitTemplate,
            Tracer tracer) {
        this.repository = repository;
        this.rabbitTemplate = rabbitTemplate;
        this.tracer = tracer;
    }

    @Scheduled(fixedDelayString = "${app.outbox.relay.delay-ms:3000}")
    @Transactional
    public void processOutbox() {
        List<OutboxEventEntity> pendingEvents = repository.findUnprocessedEvents(PageRequest.of(0, batchSize));

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
                MessageProperties props = new MessageProperties();
                props.setContentType(MessageProperties.CONTENT_TYPE_JSON);
                Message message = new Message(
                        entity.getPayload().getBytes(java.nio.charset.StandardCharsets.UTF_8),
                        props);

                rabbitTemplate.send(
                        RabbitMQConfig.EXCHANGE_EVENTS,
                        entity.getEventType(),
                        message);

                entity.setProcessed(true);
                entity.setProcessedAt(OffsetDateTime.now());
                repository.save(entity);

                log.debug("Evento '{}' (ID: {}) enviado. Outbox atualizado.", entity.getEventType(), entity.getId());

            } catch (Exception e) {
                relaySpan.error(e);
                log.error("Erro inesperado ao enviar evento ID {} - será tentado novamente.", entity.getId(), e);
                
                entity.setRetryCount(entity.getRetryCount() + 1);
                entity.setErrorMessage(e.getMessage());
                
                if (entity.getRetryCount() >= maxRetries) {
                    entity.setDead(true);
                    log.error("Evento ID {} atingiu o limite de {} tentativas e foi marcado como DEAD.", entity.getId(), maxRetries);
                }
                
                repository.save(entity);
            } finally {
                relaySpan.end();
            }
        }
    }
}
