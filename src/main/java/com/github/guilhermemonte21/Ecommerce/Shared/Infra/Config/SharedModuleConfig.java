package com.github.guilhermemonte21.Ecommerce.Shared.Infra.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Gateway.*;
import com.github.guilhermemonte21.Ecommerce.Shared.Infra.Gateway.IdempotencyGatewayImpl;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class SharedModuleConfig {

    @Bean
    public IdempotencyGateway idempotencyGateway(RedisTemplate<String, Object> redisTemplate) {
        return new IdempotencyGatewayImpl(redisTemplate);
    }
}
