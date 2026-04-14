package com.github.guilhermemonte21.Ecommerce.Shared.Infra.Jobs;

import com.github.guilhermemonte21.Ecommerce.Shared.Infra.Config.RabbitMQConfig;
import com.github.guilhermemonte21.Ecommerce.Shared.Infra.Persistence.Entity.Data.OutboxEventEntity;
import com.github.guilhermemonte21.Ecommerce.Shared.Infra.Persistence.JpaRepository.JpaOutboxEventRepository;
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

    public OutboxRelayJob(JpaOutboxEventRepository repository, RabbitTemplate rabbitTemplate) {
        this.repository = repository;
        this.rabbitTemplate = rabbitTemplate;
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
            try {
                rabbitTemplate.convertAndSend(
                        RabbitMQConfig.EXCHANGE_EVENTS,
                        entity.getEventType(),
                        entity.getPayload());

                entity.setProcessed(true);
                entity.setProcessedAt(OffsetDateTime.now());
                repository.save(entity);

                log.debug("Evento '{}' (ID: {}) enviado. Outbox atualizado.", entity.getEventType(), entity.getId());

            } catch (Exception e) {
                log.error("Erro inesperado ao enviar evento ID {} - será tentado novamente.", entity.getId(), e);
                entity.setErrorMessage(e.getMessage());
                repository.save(entity);
            }
        }
    }
}
