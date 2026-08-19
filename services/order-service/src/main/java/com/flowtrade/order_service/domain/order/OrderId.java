package com.flowtrade.order_service.domain.order;

import java.util.Objects;
import java.util.UUID;

import com.flowtrade.order_service.constants.response.order.OrderResponseConstants;

public record OrderId(UUID value) {

    public OrderId {
        if (Objects.isNull(value)) {
            throw new IllegalArgumentException(OrderResponseConstants.INVALID_ORDER_ID);
        }
    }

    public static OrderId generate() {
        return new OrderId(UUID.randomUUID());
    }
}