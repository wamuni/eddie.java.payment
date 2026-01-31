package eddie.payment.orders.payment;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record Payment (
	long id,
	long orderId,
	String idempotencyKey,
	PaymentStatus status,
	BigDecimal amount,
	String currency,
	String method,
	String failureReason,
	OffsetDateTime createdAt,
	OffsetDateTime updatedAt
) {};
