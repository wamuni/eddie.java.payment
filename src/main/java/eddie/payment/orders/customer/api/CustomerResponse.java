package eddie.payment.orders.customer.api;

import eddie.payment.orders.customer.Customer;
import java.time.OffsetDateTime;

public record CustomerResponse(
	long id,
	String email,
	String name,
	OffsetDateTime createdAt
) {
	public static CustomerResponse from(Customer c) {
		return new CustomerResponse(c.id(), c.email(), c.name(), c.createdAt());
	}
}
