package com.flowtrade.order_service.metrics.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.shaded.io.opentelemetry.proto.metrics.v1.HistogramDataPoint;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.flowtrade.order_service.application.CreateOrderUseCase;
import com.flowtrade.order_service.constants.metrics.OrderCreateMetricsConstants;
import com.flowtrade.order_service.constants.metrics.order.OrderCreateResultEnum;
import com.flowtrade.order_service.constants.tracing.OrderCreateTracingConstants;
import com.flowtrade.order_service.domain.order.IdempotencyKey;
import com.flowtrade.order_service.domain.order.Order;
import com.flowtrade.order_service.domain.order.OrderType;
import com.flowtrade.order_service.domain.order.Price;
import com.flowtrade.order_service.domain.order.Side;
import com.flowtrade.order_service.infra.tracing.MockTracer;
import com.flowtrade.order_service.repo.IdempotencyKeyStore;
import com.flowtrade.order_service.repo.InMemFailingRepository;
import com.flowtrade.order_service.repo.InMemoryRepository;
import com.flowtrade.order_service.repo.KeyStoreDB;
import com.flowtrade.order_service.repo.OrderRepository;

import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.sdk.metrics.SdkMeterProvider;
import io.opentelemetry.sdk.metrics.data.HistogramPointData;
import io.opentelemetry.sdk.metrics.data.MetricData;
import io.opentelemetry.sdk.testing.exporter.InMemoryMetricReader;

@ExtendWith(MockitoExtension.class)
public class OrderCreateMetricsTest {

	@Nested
	class OrderCreateTest {

		@Test
		void shouldIncrementSuccessCounter() {
			// Arrange
			InMemoryMetricReader metricReader = InMemoryMetricReader.create();

			SdkMeterProvider meterProvider = SdkMeterProvider.builder()
					.registerMetricReader(metricReader)
					.build();

			Meter meter = meterProvider.get("flowtrade-order-service-test");

			OrderCreateMetrics metrics = new OrderCreateMetrics(meter);

			// Act
			metrics.orderCreated();

			// Assert
			Collection<MetricData> metricData = metricReader.collectAllMetrics();

			MetricData createdMetric = metricData.stream()
					.filter(data -> data.getName().equals(
							OrderCreateMetricsConstants.ORDERS_CREATE_TOTAL))
					.findFirst()
					.orElseThrow();

			long value = createdMetric
					.getLongSumData()
					.getPoints()
					.iterator()
					.next()
					.getValue();

			assertThat(value).isEqualTo(1);
		}

		@Test
		void shouldIncrementIdempotencyCounter() {
			// Arrange
			InMemoryMetricReader metricReader = InMemoryMetricReader.create();

			SdkMeterProvider meterProvider = SdkMeterProvider.builder()
					.registerMetricReader(metricReader)
					.build();

			Meter meter = meterProvider.get("flowtrade-order-service-test");

			OrderCreateMetrics metrics = new OrderCreateMetrics(meter);

			// Act
			metrics.orderCreatedFromIdempotency();

			// Assert
			Collection<MetricData> metricData = metricReader.collectAllMetrics();

			MetricData idempotencyMetric = metricData.stream()
					.filter(data -> data.getName().equals(
							OrderCreateMetricsConstants.ORDERS_CREATE_IDEMPOTENCY_TOTAL))
					.findFirst()
					.orElseThrow();

			long value = idempotencyMetric
					.getLongSumData()
					.getPoints()
					.iterator()
					.next()
					.getValue();

			assertThat(value).isEqualTo(1);
		}

		@Test
		void shouldIncrementRejectedCounter() {
			// Arrange
			InMemoryMetricReader metricReader = InMemoryMetricReader.create();

			SdkMeterProvider meterProvider = SdkMeterProvider.builder()
					.registerMetricReader(metricReader)
					.build();

			Meter meter = meterProvider.get("flowtrade-order-service-test");

			OrderCreateMetrics metrics = new OrderCreateMetrics(meter);

			// Act
			metrics.orderRejected();

			// Assert
			Collection<MetricData> metricData = metricReader.collectAllMetrics();

			MetricData rejectedMetric = metricData.stream()
					.filter(data -> data.getName().equals(
							OrderCreateMetricsConstants.ORDERS_REJECTED_TOTAL))
					.findFirst()
					.orElseThrow();

			long value = rejectedMetric
					.getLongSumData()
					.getPoints()
					.iterator()
					.next()
					.getValue();

			assertThat(value).isEqualTo(1);
		}

		@Test
		void shouldIncrementFailedCounter() {
			// Arrange
			InMemoryMetricReader metricReader = InMemoryMetricReader.create();

			SdkMeterProvider meterProvider = SdkMeterProvider.builder()
					.registerMetricReader(metricReader)
					.build();

			Meter meter = meterProvider.get("flowtrade-order-service-test");

			OrderCreateMetrics metrics = new OrderCreateMetrics(meter);

			// Act
			metrics.orderFailed();

			// Assert
			Collection<MetricData> metricData = metricReader.collectAllMetrics();

			MetricData failedMetric = metricData.stream()
					.filter(data -> data.getName().equals(
							OrderCreateMetricsConstants.ORDERS_FAILED_TOTAL))
					.findFirst()
					.orElseThrow();

			long value = failedMetric
					.getLongSumData()
					.getPoints()
					.iterator()
					.next()
					.getValue();

			assertThat(value).isEqualTo(1);
		}

		@Test
		void shouldRecordCreateDuration() {
			// Arrange
			InMemoryMetricReader metricReader = InMemoryMetricReader.create();

			SdkMeterProvider meterProvider = SdkMeterProvider.builder()
					.registerMetricReader(metricReader)
					.build();

			Meter meter = meterProvider.get("flowtrade-order-service-test");

			OrderCreateMetrics metrics = new OrderCreateMetrics(meter);

			// Act
			metrics.recordCreateDuration(
					TimeUnit.MILLISECONDS.toNanos(150),
					OrderCreateResultEnum.CREATED);

			// Assert
			Collection<MetricData> metricData = metricReader.collectAllMetrics();

			MetricData durationMetric = metricData.stream()
					.filter(data -> data.getName().equals(
							OrderCreateMetricsConstants.ORDERS_CREATE_DURATION))
					.findFirst()
					.orElseThrow();

			HistogramPointData point = durationMetric
					.getHistogramData()
					.getPoints()
					.iterator()
					.next();

			assertThat(point.getCount()).isEqualTo(1);
			assertThat(point.getSum()).isEqualTo(150);
		}

		@Test
		void shouldRecordSuccessMetricWhenCreatingOrder() {
			// Arrange
			OrderRepository repo = new InMemoryRepository();
			KeyStoreDB<Order> keyStoreDB = new IdempotencyKeyStore<>();

			Tracer tracer = MockTracer.mockTracer(
					OrderCreateTracingConstants.ORDER_CREATE_SPAN);

			OrderCreateMetrics metrics = mock(OrderCreateMetrics.class);

			CreateOrderUseCase useCase = new CreateOrderUseCase(repo, keyStoreDB, tracer, metrics);

			IdempotencyKey idempotencyKey = new IdempotencyKey("some-id_key");

			// Act
			useCase.createOrder(
					10,
					Side.BUY,
					OrderType.LIMIT,
					new Price(new BigDecimal("200")),
					idempotencyKey);

			// Assert
			verify(metrics).orderCreated();

			verify(metrics).recordCreateDuration(
					anyLong(),
					eq(OrderCreateResultEnum.CREATED));

			verify(metrics, never()).orderRejected();
			verify(metrics, never()).orderFailed();
			verify(metrics, never()).orderCreatedFromIdempotency();
		}

		@Test
		void shouldRecordIdempotencyMetricWhenCreatingSameOrder() {
			// Arrange
			OrderRepository repo = new InMemoryRepository();
			KeyStoreDB<Order> keyStoreDB = new IdempotencyKeyStore<>();

			Tracer tracer = MockTracer.mockTracer(
					OrderCreateTracingConstants.ORDER_CREATE_SPAN);

			OrderCreateMetrics metrics = mock(OrderCreateMetrics.class);

			CreateOrderUseCase useCase = new CreateOrderUseCase(repo, keyStoreDB, tracer, metrics);

			IdempotencyKey idempotencyKey = new IdempotencyKey("some-id_key");

			// Act
			useCase.createOrder(
					10,
					Side.BUY,
					OrderType.LIMIT,
					new Price(new BigDecimal("200")),
					idempotencyKey);

			useCase.createOrder(
					10,
					Side.BUY,
					OrderType.LIMIT,
					new Price(new BigDecimal("200")),
					idempotencyKey);

			// Assert
			
			verify(metrics).orderCreatedFromIdempotency();
			verify(metrics).recordCreateDuration(
				anyLong(),
				eq(OrderCreateResultEnum.IDEMPOTENT_REPLAY));
				
			verify(metrics, never()).orderCreated();
			verify(metrics, never()).orderRejected();
			verify(metrics, never()).orderFailed();
		}

		@Test
		void shouldRecordRejectedMetricWhenCreatingOrder() {
			// Arrange
			OrderRepository repo = new InMemoryRepository();
			KeyStoreDB<Order> keyStoreDB = new IdempotencyKeyStore<>();

			Tracer tracer = MockTracer.mockTracer(
					OrderCreateTracingConstants.ORDER_CREATE_SPAN);

			OrderCreateMetrics metrics = mock(OrderCreateMetrics.class);

			CreateOrderUseCase useCase = new CreateOrderUseCase(repo, keyStoreDB, tracer, metrics);

		
			// Act
			useCase.createOrder(
					20,
					Side.BUY,
					OrderType.LIMIT,
					new Price(new BigDecimal("200")),
					null);

			// Assert
			verify(metrics).orderRejected();
			
			verify(metrics).recordCreateDuration(
				anyLong(),
				eq(OrderCreateResultEnum.REJECTED));
				
			verify(metrics, never()).orderCreated();
			verify(metrics, never()).orderFailed();
			verify(metrics, never()).orderCreatedFromIdempotency();
		}

		@Test
		void shouldRecordErrorMetricWhenCreatingOrder() {
			// Arrange
			OrderRepository repo = new InMemFailingRepository();
			KeyStoreDB<Order> keyStoreDB = new IdempotencyKeyStore<>();

			Tracer tracer = MockTracer.mockTracer(
					OrderCreateTracingConstants.ORDER_CREATE_SPAN);

			OrderCreateMetrics metrics = mock(OrderCreateMetrics.class);

			CreateOrderUseCase useCase = new CreateOrderUseCase(repo, keyStoreDB, tracer, metrics);

			IdempotencyKey idempotencyKey = new IdempotencyKey("some-id_key");

			// Act
			useCase.createOrder(
					20,
					Side.BUY,
					OrderType.LIMIT,
					new Price(new BigDecimal("200")),
					idempotencyKey);

			// Assert
			verify(metrics).orderFailed();
			
			verify(metrics).recordCreateDuration(
				anyLong(),
				eq(OrderCreateResultEnum.FAILED));
				
			verify(metrics, never()).orderRejected();
			verify(metrics, never()).orderCreated();
			verify(metrics, never()).orderCreatedFromIdempotency();
		}
	}
}
