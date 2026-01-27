create table orders (
	id bigserial primary key,
	customer_id bigint not null references customers(id),
	status varchar(32) not null,
	currency char(3) not null,
	total_amount numeric(19, 4) not null,
	create_at timestamptz not null default now()
);

create table order_items (
	id bigserial primary key,
	order_id bigint not null references orders(id) on delete cascade,
	product_id bigint not null references products(id),
	quantity int not null check (quantity > 0),
	unit_price_amount numeric(19, 4) not null,
	line_amount numeric(19, 4) not null
);

create index idx_orders_customer_id on orders(customer_id);
create index idx_order_items_order_id on order_items(order_id);
