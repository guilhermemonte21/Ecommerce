package com.github.guilhermemonte21.Ecommerce.Shared.Infra.Event.Consumer;

import com.github.guilhermemonte21.Ecommerce.Shared.Application.Port.AlertGateway;
import com.github.guilhermemonte21.Ecommerce.Shared.Infra.Config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class DeadLetterQueueConsumer {

    private static final Logger log = LoggerFactory.getLogger(DeadLetterQueueConsumer.class);
    
    private final AlertGateway alertGateway;

    public DeadLetterQueueConsumer(AlertGateway alertGateway) {
        this.alertGateway = alertGateway;
    }

    @RabbitListener(queues = {
            RabbitMQConfig.QUEUE_LIMPAR_CARRINHO_DLQ,
            RabbitMQConfig.QUEUE_CONFIRMAR_PAGAMENTO_DLQ,
            RabbitMQConfig.QUEUE_ROLLBACK_ESTOQUE_DLQ,
            RabbitMQConfig.QUEUE_NOTIF_PEDIDO_CRIADO_DLQ,
            RabbitMQConfig.QUEUE_NOTIF_PAGAMENTO_CONCLUIDO_DLQ,
            RabbitMQConfig.QUEUE_NOTIF_PEDIDO_CANCELADO_DLQ
    })
    public void processFailedEvents(Message failedMessage) {
        String queue = failedMessage.getMessageProperties().getConsumerQueue();
        String body = new String(failedMessage.getBody());

        log.error("CRITICAL: Mensagem falhou definitivamente e foi movida para a DLQ: '{}'.", queue);
        log.error("Conteúdo da mensagem falha: {}", body);

        try {
            alertGateway.enviarAlertaCritico(
                    "Falha Crítica: Mensagem em DLQ (" + queue + ")",
                    body
            );
        } catch (Exception e) {
            log.error("FALHA AO ENVIAR ALERTA DE DLQ: {}. A mensagem permanecerá em log para auditoria.", e.getMessage());
        }
    }
}
