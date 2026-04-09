package com.github.guilhermemonte21.Ecommerce.Shared.Infra.Scheduler;

import com.github.guilhermemonte21.Ecommerce.Shared.Infra.Persistence.JpaRepository.JpaIdempotencyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Component
public class IdempotencyCleanupScheduler {

    private static final Logger log = LoggerFactory.getLogger(IdempotencyCleanupScheduler.class);

    private final JpaIdempotencyRepository repository;

    public IdempotencyCleanupScheduler(JpaIdempotencyRepository repository) {
        this.repository = repository;
    }

    @Scheduled(fixedRate = 3600000)
    @Transactional
    public void cleanupExpiredRecords() {
        OffsetDateTime cutoff = OffsetDateTime.now().minusHours(24);
        int deleted = repository.deleteByCreatedAtBefore(cutoff);
        if (deleted > 0) {
            log.info("Limpeza de idempotência: {} registros expirados removidos", deleted);
        }
    }
}
