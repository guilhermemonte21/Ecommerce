package com.github.guilhermemonte21.Ecommerce.Shared.Infra.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Gateway.*;

@Configuration
public class SharedModuleConfig {


    @Bean
    public IdempotencyGateway idempotencyGateway(com.github.guilhermemonte21.Ecommerce.Shared.Infra.Persistence.JpaRepository.JpaIdempotencyRepository repository,
                                                 com.github.guilhermemonte21.Ecommerce.Shared.Infra.Mappers.IdempotencyRecordMapper mapper) {
        return new com.github.guilhermemonte21.Ecommerce.Shared.Infra.Gateway.IdempotencyGatewayImpl(repository, mapper);
    }
}
