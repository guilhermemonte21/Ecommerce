package com.github.guilhermemonte21.Ecommerce.Shared.Infra.Persistence.JpaRepository;

import com.github.guilhermemonte21.Ecommerce.Shared.Infra.Persistence.Entity.Data.IdempotencyRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Optional;

@Repository
public interface JpaIdempotencyRepository extends JpaRepository<IdempotencyRecordEntity, String> {
    Optional<IdempotencyRecordEntity> findByIdempotencyKey(String idempotencyKey);

    @Modifying
    @Query("DELETE FROM IdempotencyRecordEntity e WHERE e.createdAt < :cutoff")
    int deleteByCreatedAtBefore(OffsetDateTime cutoff);
}
