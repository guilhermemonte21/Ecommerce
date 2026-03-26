package com.github.guilhermemonte21.Ecommerce.Application.Gateway;

import com.github.guilhermemonte21.Ecommerce.Domain.Entity.IdempotencyRecord;

import java.util.Optional;

public interface IdempotencyGateway {
    Optional<IdempotencyRecord> findByKey(String key);
    IdempotencyRecord save(IdempotencyRecord record);
}
