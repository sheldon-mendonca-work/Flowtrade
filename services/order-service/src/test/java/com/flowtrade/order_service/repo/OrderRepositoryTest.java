package com.flowtrade.order_service.repo;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.flowtrade.order_service.application.CreateOrderUseCase;
import com.flowtrade.order_service.constants.tracing.OrderCreateTracingConstants;
import com.flowtrade.order_service.domain.order.IdempotencyKey;
import com.flowtrade.order_service.domain.order.Order;
import com.flowtrade.order_service.domain.order.OrderType;
import com.flowtrade.order_service.domain.order.Price;
import com.flowtrade.order_service.domain.order.Side;
import com.flowtrade.order_service.infra.tracing.MockTracer;
import com.flowtrade.order_service.metrics.OrderCreateMetricsMock;
import com.flowtrade.order_service.metrics.order.OrderCreateMetrics;

import io.opentelemetry.api.trace.Tracer;

public class OrderRepositoryTest {
  @Test
  void shouldPersistCreatedOrder(){
    InMemoryRepository repo = new InMemoryRepository();
    KeyStoreDB<Order> idempotencyKeyStore = new IdempotencyKeyStore<>();
    Tracer tracer = MockTracer.mockTracer(OrderCreateTracingConstants.ORDER_CREATE_SPAN);
      OrderCreateMetrics metrics = OrderCreateMetricsMock.mockMetrics();
      

    CreateOrderUseCase useCase = new CreateOrderUseCase(repo, idempotencyKeyStore, tracer, metrics);
    IdempotencyKey key = new IdempotencyKey("string");

    Order order = useCase.createOrder(10, Side.BUY, OrderType.LIMIT, new Price(new BigDecimal("100.0")), key);

    assertThat(repo.save(order)).isSameAs(order);
  }
}
