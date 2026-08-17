package com.flowtrade.order_service.repo;

import com.flowtrade.order_service.domain.order.IdempotencyKey;

public interface KeyStoreDB<T> {
  public void save(IdempotencyKey key, T payload);
  public T get(IdempotencyKey key);
}
