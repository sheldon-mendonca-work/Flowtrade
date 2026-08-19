package com.flowtrade.order_service.dto.order;

import java.math.BigDecimal;

import com.flowtrade.order_service.domain.order.OrderType;
import com.flowtrade.order_service.domain.order.Side;

public record CreateOrderRequestDTO(
    int quantity,
    Side side,
    OrderType orderType,
    BigDecimal price
) {}