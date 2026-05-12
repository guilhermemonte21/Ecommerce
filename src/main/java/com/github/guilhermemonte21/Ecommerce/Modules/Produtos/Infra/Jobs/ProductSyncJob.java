package com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Infra.Jobs;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductSyncJob {

    @Bean
    public CommandLineRunner syncProducts(ProductSyncService syncService) {
        return args -> syncService.syncAll();
    }
}
