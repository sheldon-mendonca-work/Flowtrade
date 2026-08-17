package com.flowtrade.order_service.repo;

import com.flowtrade.order_service.domain.order.Order;

public interface OrderRepository {
  
  public Order save(Order order);
}
