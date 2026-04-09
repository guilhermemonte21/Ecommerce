package com.github.guilhermemonte21.Ecommerce.Infra.Jobs;

import com.github.guilhermemonte21.Ecommerce.Application.Gateway.PedidoGateway;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Pagamento.IPagamento;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Pedidos;
import com.github.guilhermemonte21.Ecommerce.Domain.Enum.StatusPedido;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;

@Component
public class ScheduledCancelamentoPedidos {

    private static final Logger log = LoggerFactory.getLogger(ScheduledCancelamentoPedidos.class);

    private final PedidoGateway pedidoGateway;
    private final IPagamento pagamentoUseCase;

    public ScheduledCancelamentoPedidos(PedidoGateway pedidoGateway, IPagamento pagamentoUseCase) {
        this.pedidoGateway = pedidoGateway;
        this.pagamentoUseCase = pagamentoUseCase;
    }

    @Scheduled(fixedRate = 120000)
    public void cancelarPedidosExpirados() {
        OffsetDateTime threshold = OffsetDateTime.now().minusMinutes(10);
        log.info("Iniciando varredura de pedidos PENDENTES criados antes de {}", threshold);

        List<Pedidos> pedidosAtrasados = pedidoGateway.getPedidosByStatusAndCriadoEmBefore(StatusPedido.PENDENTE,
                threshold);

        if (pedidosAtrasados.isEmpty()) {
            log.info("Nenhum pedido expirado encontrado nesta execução.");
            return;
        }

        log.info("Encontrados {} pedidos expirados. Iniciando SAGA de cancelamento.", pedidosAtrasados.size());

        for (Pedidos pedido : pedidosAtrasados) {
            try {
                pagamentoUseCase.cancelarPagamento(pedido.getId());
                log.info("Cancelamento via SAGA acionado para o pedido ID {}", pedido.getId());
            } catch (Exception e) {
                log.error("Erro ao tentar acionar o cancelamento do pedido ID {}: {}", pedido.getId(), e.getMessage());
            }
        }
    }
}
