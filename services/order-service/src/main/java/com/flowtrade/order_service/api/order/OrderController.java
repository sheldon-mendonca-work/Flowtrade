package com.flowtrade.order_service.api.order;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.flowtrade.order_service.application.CreateOrderUseCase;
import com.flowtrade.order_service.constants.headers.HeaderConstants;
import com.flowtrade.order_service.domain.order.IdempotencyKey;
import com.flowtrade.order_service.domain.order.Order;
import com.flowtrade.order_service.domain.order.Price;
import com.flowtrade.order_service.dto.order.CreateOrderRequestDTO;
import com.flowtrade.order_service.dto.order.CreateOrderResponseDTO;



@RestController
public class OrderController {
  private final CreateOrderUseCase useCase;
  
  public OrderController(CreateOrderUseCase useCase) {
    this.useCase = useCase;
  }
  
  @PostMapping("/orders")
  public ResponseEntity<CreateOrderResponseDTO> createOrder(
    @RequestHeader(HeaderConstants.IDEMPOTENCY_KEY_HEADER) String idempotencyKey,
    @RequestBody CreateOrderRequestDTO requestOrder
  ){
    Order createdOrder = useCase.createOrder(requestOrder.quantity(), requestOrder.side(), requestOrder.orderType(), new Price(requestOrder.price()), new IdempotencyKey(idempotencyKey));
      
    return ResponseEntity.status(HttpStatus.CREATED).body(new CreateOrderResponseDTO(createdOrder.id().value(), createdOrder.side(), createdOrder.quantity() , createdOrder.orderType(), createdOrder.price().value()));
  }

  
}
