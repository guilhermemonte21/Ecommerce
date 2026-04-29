package com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Infra.Persistence.Elasticsearch.Repository;

import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Infra.Persistence.Elasticsearch.Document.ProductDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductElasticRepository extends ElasticsearchRepository<ProductDocument, UUID> {
}
