package com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Infra.Event.Consumer;

import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Infra.Persistence.Elasticsearch.Document.ProductDocument;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Infra.Persistence.Elasticsearch.Repository.ProductElasticRepository;
import com.github.guilhermemonte21.Ecommerce.Shared.Domain.Event.ProdutoAlteradoEvent;
import com.github.guilhermemonte21.Ecommerce.Shared.Infra.Config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ProductElasticSyncConsumer {

    private static final Logger log = LoggerFactory.getLogger(ProductElasticSyncConsumer.class);

    private final ProductElasticRepository repository;

    public ProductElasticSyncConsumer(ProductElasticRepository repository) {
        this.repository = repository;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_SYNC_ELASTIC)
    public void onProdutoAlterado(ProdutoAlteradoEvent event) {
        log.info("Recebido ProdutoAlteradoEvent: id={}, tipo={}", event.getId(), event.getTipoAlteracao());

        if ("DELETADO".equals(event.getTipoAlteracao())) {
            repository.deleteById(event.getId());
            log.info("Produto {} removido do Elasticsearch.", event.getId());
            return;
        }

        ProductDocument doc = ProductDocument.builder()
                .id(event.getId())
                .nomeProduto(event.getNomeProduto())
                .vendedorId(event.getVendedorId())
                .descricao(event.getDescricao())
                .preco(event.getPreco())
                .estoque(event.getEstoque())
                .build();

        repository.save(doc);
        log.info("Produto {} sincronizado no Elasticsearch.", event.getId());
    }
}
