package com.flowtrade.order_service.dto.order;

import java.math.BigDecimal;
import java.util.UUID;

import com.flowtrade.order_service.domain.order.OrderType;
import com.flowtrade.order_service.domain.order.Side;

public record CreateOrderResponseDTO(
  UUID id,
  Side side,
  int quantity,
  OrderType orderType,
  BigDecimal price
) {}
