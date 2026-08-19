package com.flowtrade.order_service.domain.order;

import java.math.BigDecimal;

import com.flowtrade.order_service.constants.response.order.OrderResponseConstants;

public record Price(BigDecimal value) {
  public Price {
    if (value == null) {
      throw new IllegalArgumentException(OrderResponseConstants.PRICE_IS_NOT_NULL);
    }

    if (value.signum() <= 0) {
      throw new IllegalArgumentException(OrderResponseConstants.PRICE_IS_INVALID);
    }
  }
}
