create table customers (
	id bigserial primary key,
	email varchar(255) not null unique,
	name varchar(100) not null,
	create_at timestamptz not null default now()
);

create table products (
	id bigserial primary key,
	sku varchar(64) not null unique,
	name varchar(255) not null,
	price_amount numeric(19, 4) not null,
	currency char(3) not null,
	active boolean not null default true,
	create_at timestamptz not null default now()
);
