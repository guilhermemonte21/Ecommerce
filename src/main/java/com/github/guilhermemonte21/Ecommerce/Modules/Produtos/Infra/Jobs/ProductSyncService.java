package com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Infra.Jobs;

import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.Gateway.ProdutoBatchGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.Mappers.ProdutoMapperApl;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Domain.Entity.Produtos;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Infra.Persistence.Elasticsearch.Document.ProductDocument;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Infra.Persistence.Elasticsearch.Repository.ProductElasticRepository;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.Gateway.UsuarioGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Domain.Entity.Usuarios;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductSyncService {

    private static final Logger log = LoggerFactory.getLogger(ProductSyncService.class);
    private static final int PAGE_SIZE = 100;

    private final ProdutoBatchGateway sqlGateway;
    private final ProductElasticRepository elasticRepository;
    private final UsuarioGateway usuarioGateway;
    private final ProdutoMapperApl mapper;

    public ProductSyncService(ProdutoBatchGateway sqlGateway,
            ProductElasticRepository elasticRepository,
            UsuarioGateway usuarioGateway,
            ProdutoMapperApl mapper) {
        this.sqlGateway = sqlGateway;
        this.elasticRepository = elasticRepository;
        this.usuarioGateway = usuarioGateway;
        this.mapper = mapper;
    }

    public void syncAll() {
        log.info("Iniciando sincronização inicial SQL -> Elasticsearch...");
        int page = 0;
        long totalSynced = 0;
        Page<Produtos> productsPage;

        do {
            productsPage = sqlGateway.findAll(PageRequest.of(page++, PAGE_SIZE));
            List<Produtos> products = productsPage.getContent();

            if (products.isEmpty())
                break;

            List<UUID> vendorIds = products.stream()
                    .map(Produtos::getVendedorId)
                    .filter(java.util.Objects::nonNull)
                    .distinct()
                    .toList();

            Map<UUID, String> vendorNames = usuarioGateway.findAllByIds(vendorIds).stream()
                    .collect(Collectors.toMap(Usuarios::getId, Usuarios::getNome));

            List<ProductDocument> documents = products.stream()
                    .map(p -> mapper.toDocument(p,
                            vendorNames.getOrDefault(p.getVendedorId(), "Vendedor desconhecido")))
                    .toList();

            elasticRepository.saveAll(documents);
            totalSynced += documents.size();

        } while (productsPage.hasNext());

        if (totalSynced > 0) {
            log.info("Sincronização concluída. {} produtos sincronizados.", totalSynced);
        } else {
            log.info("Nenhum produto encontrado para sincronização.");
        }
    }
}
