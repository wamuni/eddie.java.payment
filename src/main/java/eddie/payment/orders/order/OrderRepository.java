package eddie.payment.orders.order;

import java.math.BigDecimal;
import java.util.Optional;

public interface OrderRepository {
	
	record OrderHeader(
		long id,
		long customerId,
		OrderStatus status,
		String currency,
		BigDecimal totalAmount,
		java.time.OffsetDateTime createdAt
	) {};

	OrderHeader insert(long customerId, OrderStatus status, String currency, BigDecimal totalAmount);
	Optional<OrderHeader> findHeaderById(long orderId);
}
