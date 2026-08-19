package com.flowtrade.order_service.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.flowtrade.order_service.domain.order.IdempotencyKey;
import com.flowtrade.order_service.domain.order.Order;
import com.flowtrade.order_service.domain.order.OrderType;
import com.flowtrade.order_service.domain.order.Price;
import com.flowtrade.order_service.domain.order.Side;
import com.flowtrade.order_service.exceptions.order.InvalidIdempotencyKeyException;
import com.flowtrade.order_service.repo.IdempotencyKeyStore;
import com.flowtrade.order_service.repo.InMemFailingRepository;
import com.flowtrade.order_service.repo.InMemoryRepository;
import com.flowtrade.order_service.repo.KeyStoreDB;
import com.flowtrade.order_service.repo.OrderRepository;

public class CreateOrderTest {

  @Nested
  class CreationTest {

    @Test
    void shouldCreateOrder() {
      OrderRepository repo = new InMemoryRepository();
      KeyStoreDB<Order> idempotencyKeyStore = new IdempotencyKeyStore<>();
      CreateOrderUseCase useCase = new CreateOrderUseCase(repo, idempotencyKeyStore);
      IdempotencyKey key = new IdempotencyKey("key");

      Order order = useCase.createOrder(10,
          Side.BUY,
          OrderType.MARKET,
          null,
          key);

      assertThat(order).isNotNull();
    }

    @Test
    void shouldRejectInvalidOrderCreation() {
      OrderRepository repo = new InMemoryRepository();
      KeyStoreDB<Order> idempotencyKeyStore = new IdempotencyKeyStore<>();
      CreateOrderUseCase useCase = new CreateOrderUseCase(repo, idempotencyKeyStore);
      IdempotencyKey key = new IdempotencyKey("key");

      assertThatThrownBy(() -> useCase.createOrder(10,
          Side.BUY,
          OrderType.LIMIT,
          null,
          key)).isInstanceOf(IllegalArgumentException.class);
    }
  }

  @Nested
  class OrderRepositoryTest {
    @Test
    void shouldCreateOrderWithProvidedDetails() {
      OrderRepository repo = new InMemoryRepository();
      IdempotencyKey key = new IdempotencyKey("key");
      KeyStoreDB<Order> idempotencyKeyStore = new IdempotencyKeyStore<>();
      CreateOrderUseCase useCase = new CreateOrderUseCase(repo, idempotencyKeyStore);
      Order useCaseCreatedOrder = useCase.createOrder(10, Side.BUY, OrderType.LIMIT, new Price(new BigDecimal("100.0")),
          key);

      assertThat(useCaseCreatedOrder.quantity()).isEqualTo(10);
      assertThat(useCaseCreatedOrder.side()).isEqualTo(Side.BUY);
      assertThat(useCaseCreatedOrder.orderType()).isEqualTo(OrderType.LIMIT);
      assertThat(useCaseCreatedOrder.price()).isEqualTo(new Price(new BigDecimal("100.0")));
    }

    @Test
    void shouldPropagateOrderPersistenceFailure() {
      OrderRepository repo = new InMemFailingRepository();
      KeyStoreDB<Order> idempotencyKeyStore = new IdempotencyKeyStore<>();
      CreateOrderUseCase useCase = new CreateOrderUseCase(repo, idempotencyKeyStore);
      IdempotencyKey key = new IdempotencyKey("key");

      assertThatThrownBy(() -> useCase.createOrder(10,
          Side.BUY,
          OrderType.MARKET,
          null,
          key)).isInstanceOf(RuntimeException.class);
    }

  }

  @Nested
  class IdempotencyTests {
    @Test
    void shouldReturnExistingOrderForDuplicateIdempotencyKey() {
      OrderRepository repo = new InMemoryRepository();
      KeyStoreDB<Order> idempotencyKeyStore = new IdempotencyKeyStore<>();
      CreateOrderUseCase useCase = new CreateOrderUseCase(repo, idempotencyKeyStore);

      Order useCaseCreatedOrder1 = useCase.createOrder(10, Side.BUY, OrderType.LIMIT,
          new Price(new BigDecimal("100.0")), new IdempotencyKey("Hello"));
      Order useCaseCreatedOrder2 = useCase.createOrder(10, Side.BUY, OrderType.LIMIT,
          new Price(new BigDecimal("100.0")), new IdempotencyKey("Hello"));

      assertThat(useCaseCreatedOrder1.id()).isEqualTo(useCaseCreatedOrder2.id());
    }

    @Test
    void shouldReturnOriginalOrderWhenIdempotencyKeyIsReusedWithDifferentDetails() {
      OrderRepository repo = new InMemoryRepository();
      KeyStoreDB<Order> idempotencyKeyStore = new IdempotencyKeyStore<>();
      CreateOrderUseCase useCase = new CreateOrderUseCase(repo, idempotencyKeyStore);

      Order useCaseCreatedOrder1 = useCase.createOrder(10, Side.BUY, OrderType.LIMIT,
          new Price(new BigDecimal("100.0")), new IdempotencyKey("Hello"));
      Order useCaseCreatedOrder2 = useCase.createOrder(20, Side.SELL, OrderType.LIMIT,
          new Price(new BigDecimal("100.0")), new IdempotencyKey("Hello"));

      assertThat(useCaseCreatedOrder1.id()).isEqualTo(useCaseCreatedOrder2.id());
    }

    @Test
    void shouldRejectInvalidIdempotencyKey() {
      OrderRepository repo = new InMemoryRepository();
      KeyStoreDB<Order> idempotencyKeyStore = new IdempotencyKeyStore<>();
      CreateOrderUseCase useCase = new CreateOrderUseCase(repo, idempotencyKeyStore);

      assertThatThrownBy(
          () -> useCase.createOrder(10, Side.BUY, OrderType.LIMIT, new Price(new BigDecimal("100.0")), null))
          .isInstanceOf(InvalidIdempotencyKeyException.class);
    }

    @Test
    void shouldNotStoreIdempotencyKeyWhenPersistenceFails() {
      OrderRepository repo = new InMemFailingRepository();
      KeyStoreDB<Order> idempotencyKeyStore = new IdempotencyKeyStore<>();
      CreateOrderUseCase useCase = new CreateOrderUseCase(repo, idempotencyKeyStore);

      IdempotencyKey key = new IdempotencyKey("Hello");

      assertThatThrownBy(() -> useCase.createOrder(
          10,
          Side.BUY,
          OrderType.LIMIT,
          new Price(new BigDecimal("100.0")),
          key)).isInstanceOf(RuntimeException.class);

      assertThat(idempotencyKeyStore.get(key)).isNull();
    }
  }

}
