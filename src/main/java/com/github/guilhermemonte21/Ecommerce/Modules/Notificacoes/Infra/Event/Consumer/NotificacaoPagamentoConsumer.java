package com.github.guilhermemonte21.Ecommerce.Modules.Notificacoes.Infra.Event.Consumer;
 
import com.github.guilhermemonte21.Ecommerce.Modules.Notificacoes.Application.UseCase.EnviarEmailNotificacao;
import com.github.guilhermemonte21.Ecommerce.Shared.Domain.Event.PagamentoConcluidoEvent;
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
public class NotificacaoPagamentoConsumer {

    private static final Logger log = LoggerFactory.getLogger(NotificacaoPagamentoConsumer.class);

    private final EnviarEmailNotificacao enviarEmailNotificacao;

    public NotificacaoPagamentoConsumer(EnviarEmailNotificacao enviarEmailNotificacao) {
        this.enviarEmailNotificacao = enviarEmailNotificacao;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NOTIF_PAGAMENTO_CONCLUIDO)
    public void onPagamentoConcluido(PagamentoConcluidoEvent event, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws java.io.IOException {
        log.info("Recebido PagamentoConcluidoEvent para notificação: pedidoId={}", event.getPedidoId());

        try {
            Map<String, Object> variables = new HashMap<>();
            variables.put("nome", event.getNomeComprador());
            variables.put("pedidoId", event.getPedidoId());

            enviarEmailNotificacao.enviar(
                    event.getEmailComprador(),
                    "Pagamento Confirmado! Pedido #" + event.getPedidoId().toString().substring(0, 8),
                    "pagamento-confirmado",
                    variables
            );

            channel.basicAck(tag, false);
        } catch (Exception e) {
            log.error("Erro ao processar notificação de pagamento concluído {}: {}", event.getPedidoId(), e.getMessage());
            channel.basicNack(tag, false, false);
        }
    }
}
