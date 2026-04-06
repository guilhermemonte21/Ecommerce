package com.github.guilhermemonte21.Ecommerce.Infra.Event;

import com.github.guilhermemonte21.Ecommerce.Application.Port.EventPublisher;
import com.github.guilhermemonte21.Ecommerce.Domain.Event.DomainEvent;
import com.github.guilhermemonte21.Ecommerce.Infra.Config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQEventPublisher implements EventPublisher {

    private static final Logger log = LoggerFactory.getLogger(RabbitMQEventPublisher.class);

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publish(DomainEvent event) {
        log.info("Publicando evento '{}' na exchange '{}' com routing key '{}'",
                event.getClass().getSimpleName(),
                RabbitMQConfig.EXCHANGE_EVENTS,
                event.eventType());

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_EVENTS,
                event.eventType(),
                event
        );
    }
}
