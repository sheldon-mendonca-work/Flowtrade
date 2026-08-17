package com.flowtrade.order_service.repo;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.flowtrade.order_service.application.CreateOrderUseCase;
import com.flowtrade.order_service.domain.order.IdempotencyKey;
import com.flowtrade.order_service.domain.order.Order;
import com.flowtrade.order_service.domain.order.OrderType;
import com.flowtrade.order_service.domain.order.Price;
import com.flowtrade.order_service.domain.order.Side;

public class OrderRepositoryTest {
  @Test
  void shouldPersistCreatedOrder(){
    InMemoryRepository repo = new InMemoryRepository();
      KeyStoreDB<Order> idempotencyKeyStore = new IdempotencyKeyStore<>();
    CreateOrderUseCase useCase = new CreateOrderUseCase(repo, idempotencyKeyStore);
    IdempotencyKey key = new IdempotencyKey("string");

    Order order = useCase.createOrder(10, Side.BUY, OrderType.LIMIT, new Price(new BigDecimal("100.0")), key);

    assertThat(repo.save(order)).isSameAs(order);
  }
}
