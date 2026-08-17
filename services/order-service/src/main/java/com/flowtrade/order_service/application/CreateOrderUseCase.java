package com.flowtrade.order_service.application;

import com.flowtrade.order_service.domain.order.IdempotencyKey;
import com.flowtrade.order_service.domain.order.Order;
import com.flowtrade.order_service.domain.order.OrderType;
import com.flowtrade.order_service.domain.order.Price;
import com.flowtrade.order_service.domain.order.Side;
import com.flowtrade.order_service.exceptions.order.InvalidIdempotencyKeyException;
import com.flowtrade.order_service.repo.KeyStoreDB;
import com.flowtrade.order_service.repo.OrderRepository;

public class CreateOrderUseCase {
  private final OrderRepository orderRepository;
  private final KeyStoreDB<Order> keyStoreDB;

  public CreateOrderUseCase(OrderRepository orderRepository, KeyStoreDB<Order> keyStoreDB){
    this.orderRepository = orderRepository;
    this.keyStoreDB = keyStoreDB;
  }

  public Order createOrder(int quantity, Side side, OrderType orderType, Price price, IdempotencyKey key){
    if(key == null){
      throw new InvalidIdempotencyKeyException("Idempotency Key cannot be null");
    }
    Order existingOrder = keyStoreDB.get(key);
    if (existingOrder != null) {
      return existingOrder;
    }

    Order order = Order.create(quantity, side, orderType, price);
    Order savedOrder = orderRepository.save(order);
    keyStoreDB.save(key, savedOrder);
    return savedOrder;
  }
}
