package com.flowtrade.order_service.domain.order;

import com.flowtrade.order_service.exceptions.order.InvalidIdempotencyKeyException;

public record IdempotencyKey(String key) {
  public IdempotencyKey{
    if(key == null){
      throw new InvalidIdempotencyKeyException("Idempotency Key cannot be null");
    }

    if(key.length() == 0){
      throw new InvalidIdempotencyKeyException("Idempotency key cannot be empty");
    }

    if(key.trim().length() == 0){
      throw new InvalidIdempotencyKeyException("Idempotency key cannot be a blank string");
    }
  }
}
