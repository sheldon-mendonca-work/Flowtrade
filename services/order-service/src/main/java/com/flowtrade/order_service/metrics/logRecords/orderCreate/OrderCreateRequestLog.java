package com.flowtrade.order_service.metrics.logRecords.orderCreate;

import com.flowtrade.order_service.domain.order.OrderType;
import com.flowtrade.order_service.domain.order.Side;
import com.flowtrade.order_service.metrics.logevents.OrderCreateEvent;

public record OrderCreateRequestLog(
    OrderCreateEvent event,
    int quantity,
    Side side,
    OrderType orderType
) {} 