package com.flowtrade.order_service.domain.order;

import java.util.Objects;
import java.util.UUID;

public record OrderId(UUID value) {

    public OrderId {
        if (Objects.isNull(value)) {
            throw new IllegalArgumentException("OrderId must not be null");
        }
    }

    public static OrderId generate() {
        return new OrderId(UUID.randomUUID());
    }
}