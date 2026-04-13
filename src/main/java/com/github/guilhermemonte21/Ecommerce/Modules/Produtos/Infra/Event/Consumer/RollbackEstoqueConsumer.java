package com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Infra.Event.Consumer;

import com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions.PedidoNotFoundException;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.Gateway.PedidoGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Produtos.Application.Gateway.ProdutoGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Domain.Entity.PedidoDoVendedor;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Domain.Entity.Pedidos;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Domain.Enum.StatusPedido;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Domain.Event.PedidoCanceladoEvent;
import com.github.guilhermemonte21.Ecommerce.Shared.Infra.Config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class RollbackEstoqueConsumer {

    private static final Logger log = LoggerFactory.getLogger(RollbackEstoqueConsumer.class);

    private final PedidoGateway pedidoGateway;
    private final ProdutoGateway produtoGateway;

    public RollbackEstoqueConsumer(PedidoGateway pedidoGateway, ProdutoGateway produtoGateway) {
        this.pedidoGateway = pedidoGateway;
        this.produtoGateway = produtoGateway;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_ROLLBACK_ESTOQUE)
    @Transactional
    public void onPedidoCancelado(PedidoCanceladoEvent event) {
        log.info("Recebido PedidoCanceladoEvent: pedidoId={}, motivo='{}'",
                event.getPedidoId(), event.getMotivo());

        Pedidos pedido = pedidoGateway.getById(event.getPedidoId())
                .orElseThrow(() -> new PedidoNotFoundException(event.getPedidoId()));

        if (pedido.getStatus() == StatusPedido.CANCELADO) {
            log.warn("Pedido {} já estava CANCELADO. Mensagem duplicada ignorada.", event.getPedidoId());
            return;
        }

        Map<UUID, Long> quantidadePorProduto = new HashMap<>();
        for (PedidoDoVendedor subPedido : pedido.getItens()) {
            for (UUID produtoId : subPedido.getProdutoIds()) {
                quantidadePorProduto.merge(produtoId, 1L, Long::sum);
            }
        }

        for (Map.Entry<UUID, Long> entry : quantidadePorProduto.entrySet()) {
            produtoGateway.getById(entry.getKey()).ifPresent(p -> {
                p.atualizarEstoque(entry.getValue());
                produtoGateway.salvar(p);
                log.info("Estoque do produto '{}' revertido em {} unidade(s).",
                        p.getNomeProduto(), entry.getValue());
            });
        }

        pedido.cancelar();
        pedidoGateway.save(pedido);

        log.info("Pedido {} cancelado e estoque revertido com sucesso.", event.getPedidoId());
    }
}
