package com.flowtrade.order_service.domain.order;

import java.math.BigDecimal;
import java.util.Objects;

public record Price(BigDecimal value) {
  public Price {
    if (Objects.isNull(value)) {
      throw new IllegalArgumentException("Price must not be null");
    }

    if (value.signum() <= 0) {
      throw new IllegalArgumentException("Price must be positive");
    }
  }
}
