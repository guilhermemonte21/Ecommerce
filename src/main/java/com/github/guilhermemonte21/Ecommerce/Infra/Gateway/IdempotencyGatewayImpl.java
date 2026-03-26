package com.github.guilhermemonte21.Ecommerce.Infra.Gateway;

import com.github.guilhermemonte21.Ecommerce.Application.Gateway.IdempotencyGateway;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.IdempotencyRecord;
import com.github.guilhermemonte21.Ecommerce.Infra.Mappers.IdempotencyRecordMapper;
import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.Entity.Data.IdempotencyRecordEntity;
import com.github.guilhermemonte21.Ecommerce.Infra.Persistence.JpaRepository.JpaIdempotencyRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class IdempotencyGatewayImpl implements IdempotencyGateway {

    private final JpaIdempotencyRepository repository;
    private final IdempotencyRecordMapper mapper;

    @Override
    public Optional<IdempotencyRecord> findByKey(String key) {
        return repository.findByIdempotencyKey(key).map(mapper::toDomain);
    }

    @Override
    public IdempotencyRecord save(IdempotencyRecord record) {
        IdempotencyRecordEntity entity = mapper.toEntity(record);
        IdempotencyRecordEntity saved = repository.save(entity);
        return mapper.toDomain(saved);
    }
}
