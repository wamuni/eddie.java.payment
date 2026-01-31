package eddie.payment.orders.payment.api;

import eddie.payment.orders.payment.Payment;
import eddie.payment.orders.payment.PaymentStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record PaymentResponse(
	long id,
	long orderId,
	PaymentStatus status,
	BigDecimal amount,
	String currency,
	String method,
	String failureReason,
	OffsetDateTime createdAt,
	OffsetDateTime udpatedAt
) {
	public static PaymentResponse from(Payment p) {
		return new PaymentResponse(
			p.id(), p.orderId(), p.status(), p.amount(), p.currency(), p.method(),
			p.failureReason(), p.createdAt(), p.updatedAt()
		);
	}
};
