package com.github.guilhermemonte21.Ecommerce.Shared.Infra.Mappers;

import com.github.guilhermemonte21.Ecommerce.Shared.Domain.Entity.IdempotencyRecord;
import com.github.guilhermemonte21.Ecommerce.Shared.Infra.Persistence.Entity.Data.IdempotencyRecordEntity;
import org.springframework.stereotype.Component;

@Component
public class IdempotencyRecordMapper {

    public IdempotencyRecord toDomain(IdempotencyRecordEntity entity) {
        if (entity == null) return null;
        return IdempotencyRecord.builder()
                .idempotencyKey(entity.getIdempotencyKey())
                .httpMethod(entity.getHttpMethod())
                .requestPath(entity.getRequestPath())
                .responseStatus(entity.getResponseStatus())
                .responseBody(entity.getResponseBody())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public IdempotencyRecordEntity toEntity(IdempotencyRecord domain) {
        if (domain == null) return null;
        return IdempotencyRecordEntity.builder()
                .idempotencyKey(domain.getIdempotencyKey())
                .httpMethod(domain.getHttpMethod())
                .requestPath(domain.getRequestPath())
                .responseStatus(domain.getResponseStatus())
                .responseBody(domain.getResponseBody())
                .createdAt(domain.getCreatedAt())
                .build();
    }
}
