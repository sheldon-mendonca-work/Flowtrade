package com.flowtrade.order_service.logging.records.order;

import com.flowtrade.order_service.domain.order.OrderType;
import com.flowtrade.order_service.domain.order.Side;
import com.flowtrade.order_service.logging.events.order.OrderCreateEvent;

public record OrderCreateRequestLog(
    OrderCreateEvent event,
    int quantity,
    Side side,
    OrderType orderType
) {} 