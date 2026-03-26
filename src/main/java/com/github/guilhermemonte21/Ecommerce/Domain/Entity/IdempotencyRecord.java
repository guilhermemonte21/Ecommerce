package com.github.guilhermemonte21.Ecommerce.Domain.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IdempotencyRecord {
    private String idempotencyKey;
    private String httpMethod;
    private String requestPath;
    private int responseStatus;
    private String responseBody;
    private OffsetDateTime createdAt;
}
