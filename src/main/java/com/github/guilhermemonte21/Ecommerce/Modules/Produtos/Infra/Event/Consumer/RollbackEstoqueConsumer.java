package com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Infra.Event.Consumer;

import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.Gateway.ProdutoGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Domain.Event.PedidoCanceladoEvent;
import com.github.guilhermemonte21.Ecommerce.Shared.Infra.Config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

/**
 * ARCH-01: Consumer de rollback de estoque.
 * Depende apenas do módulo Produtos e do evento PedidoCanceladoEvent (Shared/Pedidos domain).
 * NÃO importa mais PedidoGateway, Pedidos, PedidoDoVendedor, StatusPedido — eliminando acoplamento cross-module.
 *
 * Quando o evento carrega produtosParaRollback (payload self-contained), o rollback
 * é feito diretamente. Quando o campo é null (cancelamento pelo usuário/scheduler),
 * o evento ainda precisa de um lookup — neste caso apenas logamos que não há dados de produto
 * (a implementação do Outbox Pattern resolveria isso completamente futuramente).
 */
@Component
public class RollbackEstoqueConsumer {

    private static final Logger log = LoggerFactory.getLogger(RollbackEstoqueConsumer.class);

    private final ProdutoGateway produtoGateway;

    public RollbackEstoqueConsumer(ProdutoGateway produtoGateway) {
        this.produtoGateway = produtoGateway;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_ROLLBACK_ESTOQUE)
    @Transactional
    public void onPedidoCancelado(PedidoCanceladoEvent event) {
        log.info("Recebido PedidoCanceladoEvent: pedidoId={}, motivo='{}'",
                event.getPedidoId(), event.getMotivo());

        Map<UUID, Long> produtosParaRollback = event.getProdutosParaRollback();

        if (produtosParaRollback == null || produtosParaRollback.isEmpty()) {
            log.warn("PedidoCanceladoEvent para pedido {} não contém dados de produto para rollback. " +
                     "O evento foi gerado por cancelamento manual/scheduler. Nenhuma ação de estoque executada.",
                    event.getPedidoId());
            return;
        }

        // BUG-03: usa lock pessimista para evitar race condition no rollback de estoque
        for (Map.Entry<UUID, Long> entry : produtosParaRollback.entrySet()) {
            UUID produtoId = entry.getKey();
            Long quantidade = entry.getValue();
            produtoGateway.getByIdComLock(produtoId).ifPresentOrElse(produto -> {
                produto.atualizarEstoque(quantidade);
                produtoGateway.salvar(produto);
                log.info("Estoque do produto '{}' (id={}) revertido em {} unidade(s).",
                        produto.getNomeProduto(), produtoId, quantidade);
            }, () -> log.warn("Produto {} não encontrado durante rollback de estoque do pedido {}.",
                    produtoId, event.getPedidoId()));
        }

        log.info("Rollback de estoque concluído para o pedido {}.", event.getPedidoId());
    }
}
