package eddie.payment.orders.order;

import java.util.List;
import java.time.OffsetDateTime;
import java.math.BigDecimal;

public record Order(
	long id,
	long customerId,
	OrderStatus status,
	String currency,
	BigDecimal totalAmount,
	OffsetDateTime createdAt,
	List<OrderItem> orderItems
) {};
