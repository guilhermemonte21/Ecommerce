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

    @Query("SELECT o FROM OutboxEventEntity o WHERE o.processed = false ORDER BY o.occurredOn ASC")
    List<OutboxEventEntity> findUnprocessedEvents(Pageable pageable);
}
