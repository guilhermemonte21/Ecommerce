package com.github.guilhermemonte21.Ecommerce.Application.Port;

import com.github.guilhermemonte21.Ecommerce.Domain.Event.DomainEvent;

public interface EventPublisher {
    void publish(DomainEvent event);
}
