package com.github.guilhermemonte21.Ecommerce.Shared.Infra.Persistence.JpaRepository;

import com.github.guilhermemonte21.Ecommerce.Shared.Infra.Persistence.Entity.Data.OutboxEventEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JpaOutboxEventRepository extends JpaRepository<OutboxEventEntity, UUID> {

    @Query(value = "SELECT * FROM outbox_events WHERE processed = false AND dead = false ORDER BY occurred_on ASC FOR UPDATE SKIP LOCKED", nativeQuery = true)
    List<OutboxEventEntity> findUnprocessedEvents(Pageable pageable);
}
