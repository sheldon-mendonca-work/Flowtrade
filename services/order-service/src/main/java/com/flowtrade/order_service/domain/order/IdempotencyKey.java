package com.flowtrade.order_service.domain.order;

import com.flowtrade.order_service.constants.response.HeaderResponseConstants;
import com.flowtrade.order_service.exceptions.order.InvalidIdempotencyKeyException;

public record IdempotencyKey(String key) {
  public IdempotencyKey{
    if(key == null){
      throw new InvalidIdempotencyKeyException(HeaderResponseConstants.IDEMPOTENCY_NULL);
    }

    if(key.trim().length() == 0){
      throw new InvalidIdempotencyKeyException(HeaderResponseConstants.IDEMPOTENCY_EMPTY);
    }
  }
}
