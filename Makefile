OTEL_COMPOSE_FILE := infra/openTelemetry/docker-compose.yml
POSTGRES_COMPOSE_FILE := infra/postgres/docker-compose.yml

infra-up:
	docker compose -f ${OTEL_COMPOSE_FILE} -f ${POSTGRES_COMPOSE_FILE} up -d

infra-build:
	docker compose -f ${OTEL_COMPOSE_FILE} -f ${POSTGRES_COMPOSE_FILE} build

infra-down:
	docker compose -f ${OTEL_COMPOSE_FILE} -f ${POSTGRES_COMPOSE_FILE} down

# docker compose -f infra/openTelemetry/docker-compose.yml -f infra/postgres/docker-compose.yml ps