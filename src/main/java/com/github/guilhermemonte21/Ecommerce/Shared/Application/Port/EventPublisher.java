package com.github.guilhermemonte21.Ecommerce.Shared.Application.Port;

import com.github.guilhermemonte21.Ecommerce.Shared.Domain.Event.DomainEvent;

public interface EventPublisher {
    void publish(DomainEvent event);
}
