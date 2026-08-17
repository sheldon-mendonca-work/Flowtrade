package com.flowtrade.order_service.repo;

import java.util.HashMap;
import java.util.Map;

import com.flowtrade.order_service.domain.order.IdempotencyKey;

public class IdempotencyKeyStore<T> implements KeyStoreDB<T>{
  private  final Map<IdempotencyKey, T> store;
  
  public IdempotencyKeyStore(){
    this.store = new HashMap<>();
  }
  
  @Override
  public void save(IdempotencyKey key, T payload) {
    if(store.containsKey(key)){
      return;
    }

    store.put(key, payload);
  }

  @Override
  public T get(IdempotencyKey key) {
    return store.get(key);
  }
  
}
