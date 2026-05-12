package com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Infra.Event.Consumer;

import com.github.guilhermemonte21.Ecommerce.Shared.Application.Exceptions.PedidoNotFoundException;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Application.Gateway.PedidoGateway;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Domain.Entity.Pedidos;
import com.github.guilhermemonte21.Ecommerce.Modules.Pedidos.Domain.Enum.StatusPedido;
import com.github.guilhermemonte21.Ecommerce.Shared.Domain.Event.PagamentoConcluidoEvent;
import com.github.guilhermemonte21.Ecommerce.Shared.Infra.Config.RabbitMQConfig;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ConfirmarPagamentoConsumer {

    private static final Logger log = LoggerFactory.getLogger(ConfirmarPagamentoConsumer.class);

    private final PedidoGateway pedidoGateway;

    public ConfirmarPagamentoConsumer(PedidoGateway pedidoGateway) {
        this.pedidoGateway = pedidoGateway;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_CONFIRMAR_PAGAMENTO)
    @Transactional
    public void onPagamentoConcluido(PagamentoConcluidoEvent event, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws java.io.IOException {
        log.info("Recebido PagamentoConcluidoEvent: pedidoId={}", event.getPedidoId());

        try {
            Pedidos pedido = pedidoGateway.getById(event.getPedidoId())
                    .orElseThrow(() -> new PedidoNotFoundException(event.getPedidoId()));

            if (pedido.getStatus() == StatusPedido.APROVADO) {
                log.warn("Pedido {} já estava com status APROVADO. Mensagem duplicada ignorada.",
                        event.getPedidoId());
                channel.basicAck(tag, false);
                return;
            }

            pedido.confirmarPagamento();
            pedidoGateway.save(pedido);

            log.info("Pedido {} atualizado para APROVADO com sucesso.", event.getPedidoId());
            channel.basicAck(tag, false);
        } catch (PedidoNotFoundException e) {
            log.error("Pedido {} não encontrado para confirmar pagamento", event.getPedidoId());
            channel.basicAck(tag, false);
        } catch (Exception e) {
            log.error("Erro ao processar confirmação de pagamento para o pedido {}: {}", event.getPedidoId(), e.getMessage());
            channel.basicNack(tag, false, false);
        }
    }
}
