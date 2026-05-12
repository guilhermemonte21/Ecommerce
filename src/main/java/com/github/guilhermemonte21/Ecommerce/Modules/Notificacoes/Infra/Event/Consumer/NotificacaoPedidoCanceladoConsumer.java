package com.github.guilhermemonte21.Ecommerce.Modules.Notificacoes.Infra.Event.Consumer;
 
import com.github.guilhermemonte21.Ecommerce.Modules.Notificacoes.Application.UseCase.EnviarEmailNotificacao;
import com.github.guilhermemonte21.Ecommerce.Shared.Domain.Event.PedidoCanceladoEvent;
import com.github.guilhermemonte21.Ecommerce.Shared.Infra.Config.RabbitMQConfig;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class NotificacaoPedidoCanceladoConsumer {

    private static final Logger log = LoggerFactory.getLogger(NotificacaoPedidoCanceladoConsumer.class);

    private final EnviarEmailNotificacao enviarEmailNotificacao;

    public NotificacaoPedidoCanceladoConsumer(EnviarEmailNotificacao enviarEmailNotificacao) {
        this.enviarEmailNotificacao = enviarEmailNotificacao;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NOTIF_PEDIDO_CANCELADO)
    public void onPedidoCancelado(PedidoCanceladoEvent event, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws java.io.IOException {
        log.info("Recebido PedidoCanceladoEvent para notificação: pedidoId={}", event.getPedidoId());

        try {
            Map<String, Object> variables = new HashMap<>();
            variables.put("nome", event.getNomeComprador());
            variables.put("pedidoId", event.getPedidoId());
            variables.put("motivo", event.getMotivo());

            enviarEmailNotificacao.enviar(
                    event.getEmailComprador(),
                    "Pedido Cancelado #" + event.getPedidoId().toString().substring(0, 8),
                    "pedido-cancelado",
                    variables
            );

            channel.basicAck(tag, false);
        } catch (Exception e) {
            log.error("Erro ao processar notificação de pedido cancelado {}: {}", event.getPedidoId(), e.getMessage());
            channel.basicNack(tag, false, false);
        }
    }
}
