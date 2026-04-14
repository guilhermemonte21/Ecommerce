package com.github.guilhermemonte21.Ecommerce.Shared.Infra.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Gateway.*;
import com.github.guilhermemonte21.Ecommerce.Shared.Infra.Gateway.IdempotencyGatewayImpl;
import com.github.guilhermemonte21.Ecommerce.Shared.Infra.Mappers.IdempotencyRecordMapper;
import com.github.guilhermemonte21.Ecommerce.Shared.Infra.Persistence.JpaRepository.JpaIdempotencyRepository;

@Configuration
public class SharedModuleConfig {

    @Bean
    public IdempotencyGateway idempotencyGateway(JpaIdempotencyRepository repository,
            IdempotencyRecordMapper mapper) {
        return new IdempotencyGatewayImpl(repository, mapper);
    }
}
