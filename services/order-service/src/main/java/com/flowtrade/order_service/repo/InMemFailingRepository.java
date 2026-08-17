package com.flowtrade.order_service.repo;

import com.flowtrade.order_service.domain.order.Order;

public class InMemFailingRepository implements OrderRepository{
   private Order savedOrder;
   
    @Override
    public Order save(Order order) {
        throw new RuntimeException("Persistence failed");
    }

    public Order savedOrder() {
        return savedOrder;
    }
}
