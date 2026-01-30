package eddie.payment.orders.payment;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository {
	
	Optional<Payment> findByOrderAndKey(long orderId, String idempotencyKey);

	Payment insertPending(long orderId, String idempotencyKey, BigDecimal amount, String currency, String method);

	Payment updateResult(long paymentId, PaymentStatus status, String failureReason);

	Optional<Payment> findById(long paymentId);

	List<Payment> findByOrderId(long orderId, int limit, int offset);
}
