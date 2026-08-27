package com.flowtrade.order_service.application;

import org.springframework.stereotype.Service;

import com.flowtrade.observability.logging.StructuredLogger;
import com.flowtrade.order_service.constants.response.HeaderResponseConstants;
import com.flowtrade.order_service.constants.tracing.OrderTracingConstants;
import com.flowtrade.order_service.domain.order.IdempotencyKey;
import com.flowtrade.order_service.domain.order.Order;
import com.flowtrade.order_service.domain.order.OrderType;
import com.flowtrade.order_service.domain.order.Price;
import com.flowtrade.order_service.domain.order.Side;
import com.flowtrade.order_service.exceptions.order.InvalidIdempotencyKeyException;
import com.flowtrade.order_service.metrics.logRecords.orderCreate.OrderCreateFailedLog;
import com.flowtrade.order_service.metrics.logRecords.orderCreate.OrderCreateLog;
import com.flowtrade.order_service.metrics.logRecords.orderCreate.OrderCreateRejectedLog;
import com.flowtrade.order_service.metrics.logRecords.orderCreate.OrderCreateRequestLog;
import com.flowtrade.order_service.metrics.logevents.OrderCreateEvent;
import com.flowtrade.order_service.metrics.logevents.OrderCreateRejectionReason;
import com.flowtrade.order_service.repo.KeyStoreDB;
import com.flowtrade.order_service.repo.OrderRepository;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;

@Service
public class CreateOrderUseCase {
  private final OrderRepository orderRepository;
  private final KeyStoreDB<Order> keyStoreDB;
  private final Tracer tracer;

  public CreateOrderUseCase(OrderRepository orderRepository, KeyStoreDB<Order> keyStoreDB, Tracer tracer) {
    this.orderRepository = orderRepository;
    this.keyStoreDB = keyStoreDB;
    this.tracer = tracer;
  }

  public Order createOrder(int quantity, Side side, OrderType orderType, Price price, IdempotencyKey key) {
    Order savedOrder;
    
    Span span = tracer
        .spanBuilder(OrderTracingConstants.ORDER_CREATE_SPAN)
        .startSpan();
    span.setAttribute(OrderTracingConstants.ORDER_SIDE, side.name());
    span.setAttribute(OrderTracingConstants.ORDER_TYPE, orderType.name());

    try (Scope ignored = span.makeCurrent()) {
      StructuredLogger.info(
          new OrderCreateRequestLog(
              OrderCreateEvent.ORDER_CREATE_REQUESTED,
              quantity,
              side,
              orderType
          )
        );

      if (key == null) {
        StructuredLogger.warn(
            new OrderCreateRejectedLog(
                OrderCreateEvent.ORDER_CREATE_REJECTED,
                OrderCreateRejectionReason.INVALID_IDEMPOTENCY_KEY.name()
            )
        );

        throw new InvalidIdempotencyKeyException(HeaderResponseConstants.IDEMPOTENCY_NULL);
      }
      Order existingOrder = keyStoreDB.get(key);

      if (existingOrder != null) {
        StructuredLogger.info(
            new OrderCreateLog(
                OrderCreateEvent.ORDER_CREATE_IDEMPOTENT_REPLAY,
                existingOrder.id().value().toString(),
                existingOrder.side(),
                existingOrder.orderType()));

        return existingOrder;
      }

      Order order = Order.create(quantity, side, orderType, price);
      savedOrder = orderRepository.save(order);
      
      StructuredLogger.info(
        new OrderCreateLog(
          OrderCreateEvent.ORDER_CREATED,
          order.id().value().toString(),
          order.side(),
          order.orderType()
        )
      );
      keyStoreDB.save(key, savedOrder);
      
    } catch(InvalidIdempotencyKeyException exception){
      throw exception;
    } catch (RuntimeException exception) {
      
      StructuredLogger.error(
          new OrderCreateFailedLog(
              OrderCreateEvent.ORDER_CREATE_FAILED,
              exception.getClass().getSimpleName()
          )
      );
      span.recordException(exception);
      span.setStatus(StatusCode.ERROR);
      throw exception;
    } finally {
      span.end();
    }

    return savedOrder;
  }
}
