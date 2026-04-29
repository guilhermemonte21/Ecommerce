package com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Infra.Jobs;

import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.Gateway.ProdutoGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.Mappers.ProdutoMapperApl;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Domain.Entity.Produtos;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Infra.Persistence.Elasticsearch.Document.ProductDocument;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Infra.Persistence.Elasticsearch.Repository.ProductElasticRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class ProductSyncJob {

    private static final Logger log = LoggerFactory.getLogger(ProductSyncJob.class);

    @Bean
    public CommandLineRunner syncProducts(ProdutoGateway sqlGateway,
            ProductElasticRepository elasticRepository,
            ProdutoMapperApl mapper) {
        return args -> {
            log.info("Iniciando sincronização inicial SQL -> Elasticsearch...");

            int page = 0;
            int size = 100;
            Page<Produtos> productsPage;
            long totalSynced = 0;

            do {
                productsPage = sqlGateway.findAll(PageRequest.of(page, size));
                List<ProductDocument> documents = productsPage.getContent().stream()
                        .map(mapper::toDocument)
                        .collect(Collectors.toList());

                if (!documents.isEmpty()) {
                    elasticRepository.saveAll(documents);
                    totalSynced += documents.size();
                }
                page++;
            } while (productsPage.hasNext());

            if (totalSynced > 0) {
                log.info("Sincronização concluída. {} produtos sincronizados com sucesso.", totalSynced);
            } else {
                log.info("Nenhum produto encontrado para sincronização.");
            }
        };
    }
}
