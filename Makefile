OTEL_COMPOSE_FILE := infra/openTelemetry/docker-compose.yml
POSTGRES_COMPOSE_FILE := infra/postgres/docker-compose.yml

infra-up:
	docker compose -f ${OTEL_COMPOSE_FILE} -f ${POSTGRES_COMPOSE_FILE} up -d