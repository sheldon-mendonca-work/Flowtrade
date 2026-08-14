
CREATE TABLE order_idempotency_keys (
    id UUID PRIMARY KEY,
    idempotency_key VARCHAR(255) UNIQUE NOT NULL,
    request_hash CHAR(64) NOT NULL,
    owner_token UUID NOT NULL,
    owner_instance TEXT NOT NULL,
    status VARCHAR(20) NOT NULL CHECK status in ('PENDING', 'PROCESSING', 'COMPLETED', 'FAILED'),
    response JSONB,
    http_status SMALLINT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    lease_until TIMESTAMP,
    retry_count INTEGER DEFAULT 0,
    expires_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_order_idempotency ON order_idempotency_keys(id);

CREATE TABLE users (
    id UUID PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    user_handle VARCHAR(50) NOT NULL UNIQUE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE orders (
    id UUID PRIMARY KEY,
    client_order_id VARCHAR(50) NOT NULL,
    user_id UUID NOT NULL,
    symbol VARCHAR(20) NOT NULL,
    side CHAR(4) CHECK side in ('BUY', 'SELL'),
    order_type CHAR(6) CHECK order_type in ('LIMIT', 'MARKET'),
    price NUMERIC(18,8), 
    quantity NUMERIC(18,8),
    filled_quantity NUMERIC(18,8) NOT NULL DEFAULT 0,
    status CHECK (status IN ('NEW','PARTIALLY_FILLED', 'FILLED', 'CANCELLED', 'REJECTED')) NOT NULL,    
    order_idempotency_key UUID NOT NULL,
    version BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_order_idempotency_key FOREIGN KEY (order_idempotency_key) REFERENCES order_idempotency_keys(id),
    CONSTRAINT fk_order_user_id FOREIGN KEY (user_id) REFERENCES users(id)
);


CREATE TABLE order_outbox_events (
    id UUID PRIMARY KEY,
    order_id UUID NOT NULL,
    idempotency_key UUID NOT NULL,
    event_type VARCHAR(50) NOT NULL,

    payload JSONB NOT NULL,

    status VARCHAR(20) NOT NULL
        CHECK (status IN (
            'PENDING',
            'PROCESSING',
            'PUBLISHED',
            'FAILED'
        )),

    owner_token UUID,
    locked_until TIMESTAMP,
    publish_attempts INT NOT NULL DEFAULT 0,
    next_retry_at TIMESTAMP,
    trace_id TEXT NOT NULL,
    request_id TEXT NOT NULL,
    error_code TEXT,
    error_message TEXT,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    published_at TIMESTAMP,

    CONSTRAINT fk_order_outbox_order
        FOREIGN KEY (order_id)
        REFERENCES orders(id)
);

