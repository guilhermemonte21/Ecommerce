package com.github.guilhermemonte21.Ecommerce.Shared.Infra.Gateway;

import com.github.guilhermemonte21.Ecommerce.Shared.Application.Gateway.IdempotencyGateway;
import com.github.guilhermemonte21.Ecommerce.Shared.Domain.Entity.IdempotencyRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class IdempotencyGatewayImpl implements IdempotencyGateway {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String REDIS_PREFIX = "idempotency:";
    private static final long TTL_HOURS = 24;

    @Override
    public Optional<IdempotencyRecord> findByKey(String key) {
        Object record = redisTemplate.opsForValue().get(REDIS_PREFIX + key);
        if (record instanceof IdempotencyRecord idempotencyRecord) {
            return Optional.of(idempotencyRecord);
        }
        return Optional.empty();
    }

    @Override
    public IdempotencyRecord save(IdempotencyRecord record) {
        redisTemplate.opsForValue().set(
                REDIS_PREFIX + record.getIdempotencyKey(),
                record,
                TTL_HOURS,
                TimeUnit.HOURS
        );
        return record;
    }
}
