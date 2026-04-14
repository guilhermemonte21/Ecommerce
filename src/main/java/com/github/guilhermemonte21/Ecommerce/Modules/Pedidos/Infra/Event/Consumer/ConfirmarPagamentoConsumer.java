package com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Infra.Event.Consumer;

import com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions.PedidoNotFoundException;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.Gateway.PedidoGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Domain.Entity.Pedidos;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Domain.Enum.StatusPedido;
import com.github.guilhermemonte21.Ecommerce.Modules.Pagamento.Domain.Event.PagamentoConcluidoEvent;
import com.github.guilhermemonte21.Ecommerce.Shared.Infra.Config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * ARCH-02: Consumer movido para o módulo Pedidos, que é o dono da entidade Pedidos.
 * O módulo Pagamento apenas publica o evento PagamentoConcluidoEvent — a atualização
 * do status do pedido é responsabilidade deste módulo, não do Pagamento.
 */
@Component
public class ConfirmarPagamentoConsumer {

    private static final Logger log = LoggerFactory.getLogger(ConfirmarPagamentoConsumer.class);

    private final PedidoGateway pedidoGateway;

    public ConfirmarPagamentoConsumer(PedidoGateway pedidoGateway) {
        this.pedidoGateway = pedidoGateway;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_CONFIRMAR_PAGAMENTO)
    @Transactional
    public void onPagamentoConcluido(PagamentoConcluidoEvent event) {
        log.info("Recebido PagamentoConcluidoEvent: pedidoId={}", event.getPedidoId());

        Pedidos pedido = pedidoGateway.getById(event.getPedidoId())
                .orElseThrow(() -> new PedidoNotFoundException(event.getPedidoId()));

        if (pedido.getStatus() == StatusPedido.APROVADO) {
            log.warn("Pedido {} já estava com status APROVADO. Mensagem duplicada ignorada.",
                    event.getPedidoId());
            return;
        }

        pedido.confirmarPagamento();
        pedidoGateway.save(pedido);

        log.info("Pedido {} atualizado para APROVADO com sucesso.", event.getPedidoId());
    }
}
