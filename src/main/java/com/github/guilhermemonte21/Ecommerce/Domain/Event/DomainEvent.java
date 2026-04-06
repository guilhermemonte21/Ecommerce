package com.github.guilhermemonte21.Ecommerce.Domain.Event;

import java.time.OffsetDateTime;

public interface DomainEvent {
    String eventType();
    OffsetDateTime occurredOn();
}
