package com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Infra.Jobs;

import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.Gateway.PedidoGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Domain.Entity.PedidoDoVendedor;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Domain.Entity.Pedidos;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Domain.Enum.StatusPedido;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Domain.Event.PedidoCanceladoEvent;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Port.EventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class ScheduledCancelamentoPedidos {

    private static final Logger log = LoggerFactory.getLogger(ScheduledCancelamentoPedidos.class);

    private final PedidoGateway pedidoGateway;
    private final EventPublisher eventPublisher;

    public ScheduledCancelamentoPedidos(PedidoGateway pedidoGateway, EventPublisher eventPublisher) {
        this.pedidoGateway = pedidoGateway;
        this.eventPublisher = eventPublisher;
    }

    @Scheduled(fixedRate = 120000)
    @Transactional
    public void cancelarPedidosExpirados() {
        OffsetDateTime threshold = OffsetDateTime.now().minusMinutes(10);
        log.info("Iniciando varredura de pedidos PENDENTES criados antes de {}", threshold);

        List<Pedidos> pedidosAtrasados = pedidoGateway.getPedidosByStatusAndCriadoEmBefore(StatusPedido.PENDENTE,
                threshold);

        if (pedidosAtrasados.isEmpty()) {
            log.info("Nenhum pedido expirado encontrado nesta execução.");
            return;
        }

        log.info("Encontrados {} pedidos expirados. Disparando eventos de cancelamento.", pedidosAtrasados.size());

        for (Pedidos pedido : pedidosAtrasados) {
            try {

                Map<UUID, Long> produtosParaRollback = new HashMap<>();
                for (PedidoDoVendedor item : pedido.getItens()) {
                    produtosParaRollback.merge(item.getProdutoId(), item.getQuantidade(), Long::sum);
                }

                eventPublisher.publish(new PedidoCanceladoEvent(
                        pedido.getId(),
                        "Timeout: Pagamento não realizado em tempo hábil",
                        produtosParaRollback));

                log.info("Evento de cancelamento publicado para o pedido ID {}", pedido.getId());
            } catch (Exception e) {
                log.error("Erro ao tentar cancelar o pedido ID {}: {}", pedido.getId(), e.getMessage());
            }
        }
    }
}
