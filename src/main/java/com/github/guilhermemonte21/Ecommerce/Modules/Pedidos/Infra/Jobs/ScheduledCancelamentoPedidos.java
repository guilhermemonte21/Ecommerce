package com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Infra.Jobs;

import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.Gateway.PedidoGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Domain.Entity.Pedidos;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Domain.Enum.StatusPedido;
import com.github.guilhermemonte21.Ecommerce.Shared.Domain.Event.PedidoCanceladoEvent;
import com.github.guilhermemonte21.Ecommerce.Shared.Domain.Enum.MotivoCancelamentoPedido;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Port.EventPublisher;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Application.Gateway.UsuarioGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Usuarios.Domain.Entity.Usuarios;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Component
public class ScheduledCancelamentoPedidos {

    private static final Logger log = LoggerFactory.getLogger(ScheduledCancelamentoPedidos.class);

    private final PedidoGateway pedidoGateway;
    private final EventPublisher eventPublisher;
    private final UsuarioGateway usuarioGateway;

    @Value("${app.pedidos.cancelamento.timeout-minutes:10}")
    private int timeoutMinutes;

    public ScheduledCancelamentoPedidos(PedidoGateway pedidoGateway, EventPublisher eventPublisher, UsuarioGateway usuarioGateway) {
        this.pedidoGateway = pedidoGateway;
        this.eventPublisher = eventPublisher;
        this.usuarioGateway = usuarioGateway;
    }

    @Scheduled(fixedRateString = "${app.pedidos.cancelamento.rate-ms:120000}")
    @Transactional
    public void cancelarPedidosExpirados() {
        OffsetDateTime threshold = OffsetDateTime.now().minusMinutes(timeoutMinutes);
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
                Usuarios comprador = usuarioGateway.getById(pedido.getCompradorId()).orElse(null);

                eventPublisher.publish(PedidoCanceladoEvent.por(
                        pedido.getId(),
                        MotivoCancelamentoPedido.TIMEOUT_PAGAMENTO,
                        comprador != null ? comprador.getNome() : "Cliente",
                        comprador != null ? comprador.getEmail() : "",
                        pedido.coletarItensParaRollback()));

                pedido.cancelar();
                pedidoGateway.save(pedido);

                log.info("Pedido ID {} cancelado por timeout e evento de rollback publicado.", pedido.getId());
            } catch (Exception e) {
                log.error("Erro ao tentar cancelar o pedido ID {}: {}", pedido.getId(), e.getMessage());
            }
        }
    }
}
