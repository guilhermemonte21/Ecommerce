package com.github.guilhermemonte21.Ecommerce.Modules.Pagamento.Application.UseCase.Pagamento;

import com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions.PedidoNotFoundException;
import com.github.guilhermemonte21.Ecommerce.Modules.Pagamento.Application.Gateway.PagamentoGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.Gateway.PedidoGateway;
import com.github.guilhermemonte21.Ecommerce.Shared.Application.Port.EventPublisher;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Domain.Entity.Pedidos;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Domain.Enum.StatusPedido;
import com.github.guilhermemonte21.Ecommerce.Modules.Pagamento.Domain.Event.PagamentoConcluidoEvent;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Domain.Event.PedidoCanceladoEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public class Pagamento implements IPagamento {

    private static final Logger log = LoggerFactory.getLogger(Pagamento.class);

    private final PedidoGateway pedidoGateway;
    private final PagamentoGateway pagamentoGateway;
    private final EventPublisher eventPublisher;

    public Pagamento(PedidoGateway pedidoGateway, PagamentoGateway pagamentoGateway,
                     EventPublisher eventPublisher) {
        this.pedidoGateway = pedidoGateway;
        this.pagamentoGateway = pagamentoGateway;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Boolean pagar(UUID idPedido) {
        Pedidos pedido = pedidoGateway.getById(idPedido)
                .orElseThrow(() -> new PedidoNotFoundException(idPedido));

        log.info("Iniciando processo de pagamento no Gateway (Stripe) para o pedido {}", idPedido);
        boolean sucesso = pagamentoGateway.processarPagamento(pedido);

        if (sucesso) {
            log.info("Pagamento aprovado. Publicando PagamentoConcluidoEvent para o pedido {}", idPedido);
            eventPublisher.publish(new PagamentoConcluidoEvent(idPedido));
            return true;
        }

        log.error("Pagamento não foi aprovado no Gateway para o pedido {}. Publicando cancelamento para Rollback.", idPedido);
        eventPublisher.publish(new PedidoCanceladoEvent(idPedido, "Falha no processo de pagamento"));
        return false;
    }

    @Override
    @Transactional
    public void cancelarPagamento(UUID idPedido) {
        Pedidos pedido = pedidoGateway.getById(idPedido)
                .orElseThrow(() -> new PedidoNotFoundException(idPedido));

        if (pedido.getStatus() == StatusPedido.ENTREGUE
                || pedido.getStatus() == StatusPedido.CANCELADO
                || pedido.getStatus() == StatusPedido.ENVIADO) {
            throw new IllegalStateException("Pedido já foi entregue, enviado ou cancelado");
        }

        log.info("Publicando PedidoCanceladoEvent para o pedido {}", idPedido);
        eventPublisher.publish(new PedidoCanceladoEvent(idPedido, "Cancelado pelo usuário"));
    }
}
