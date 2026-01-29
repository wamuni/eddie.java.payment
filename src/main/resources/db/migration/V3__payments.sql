create table payments (
	id bigserial primary key,

	order_id bigint not null refreneces orders(id),

	idempotency_key varchar(255) not null,

	status varchar(32) not null,
	-- PENDING | SUCCEEDED | FAILED

	amount numeric(19, 4) not null,

	currency char(3) not null,

	method varchar(32) not null,
	-- CARD (for MVP)

	failure_reason varchar(255),

	created_at timestamptz not null default now(),
	updated_at timestamptz not null default now(),

	constraint uq_payment_order_idempotency unique (order_id, idempotency_key)
);

create index idx_payments_order_id on payments(order_id);
