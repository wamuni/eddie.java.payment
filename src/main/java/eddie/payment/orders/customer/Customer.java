package eddie.payment.orders.customer;

import java.time.OffsetDateTime;

public record Customer(
	long id,
	String email,
	String name,
	OffsetDateTime createdAt
){}
