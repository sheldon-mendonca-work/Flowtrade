package com.flowtrade.order_service.repo;

import org.springframework.stereotype.Repository;

import com.flowtrade.order_service.domain.order.Order;

@Repository
public class InMemoryRepository implements OrderRepository{
  private Order savedOrder;

    @Override
    public Order save(Order order) {
        savedOrder = order;
        return order;
    }

    public Order savedOrder() {
        return savedOrder;
    }
}
