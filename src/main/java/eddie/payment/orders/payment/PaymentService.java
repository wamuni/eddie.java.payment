package eddie.payment.orders.payment;

import eddie.payment.orders.order.OrderRepository;
import eddie.payment.orders.order.OrderStatus;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.List;
import java.nio.charset.StandardCharsets;

@Service
public class PaymentService {

	private final OrderRepository orders;
	private final PaymentRepository payments;

	public PaymentService(OrderRepository orders, PaymentRepository payments) {
		this.orders = orders;
		this.payments = payments;
	}

	@Transactional
	public Payment createPayment(long orderId, String idempotencyKey, String method) {
		if (idempotencyKey == null || idempotencyKey.isBlank()) {
			throw new IllegalArgumentException("Missing Idempotency Key header");
		}
		
		// 1) IdempotencyKey replay: if exists, return it
		Optional<Payment> existing = payments.findByOrderAndKey(orderId, idempotencyKey);
		if (existing.isPresent()) return existing.get();

		// 2) Load order header
		var header = orders.findHeaderById(orderId).orElseThrow(() -> new OrderMissingException(orderId));

		if (header.status() == OrderStatus.PAID) {
			throw new OrderAlreadyPaidException(orderId);
		}
		BigDecimal amount = header.totalAmount();
		String currency = header.currency();

		// 3) Insert Pending payment;
		Payment pending;
		try {
			pending = payments.insertPending(orderId, idempotencyKey, amount, currency, method);
		} catch (DataIntegrityViolationException e) {
			return payments.findByOrderAndKey(orderId, idempotencyKey).orElseThrow(() -> e);
		}

		// 4) Simulate provider result deterministically
		SimulatedResult result = simulate(idempotencyKey);

		if (result.succeeded()) {
			// 5) Mark order Paid (guarded)
			int update = orders.makePaidIfCreated(orderId);
			if (update == 1) {
				return payments.updateResult(pending.id(), PaymentStatus.SUCCEEDED, null);
			} else {
				return payments.updateResult(pending.id(), PaymentStatus.FAILED, result.failureReason());
			}
		} else {
			return payments.updateResult(pending.id(), PaymentStatus.FAILED, result.failureReason());
		}
	}
	
	public Payment get(long paymentId) {
		return payments.findById(paymentId).orElseThrow(() -> new PaymentNotFoundException(paymentId));
	}

	public List<Payment> listForOrder(long orderId, int limit, int offset) {
		orders.findHeaderById(orderId).orElseThrow(() -> new OrderMissingException(orderId));
		int safeLimit = Math.min(Math.max(limit, 1), 100);
		int safeOffset = Math.max(offset, 0);
		return payments.findByOrderId(orderId, safeLimit, safeOffset);
	}
	
	public static class OrderMissingException extends RuntimeException {
		public OrderMissingException(long id) { super("Order Not Found: " + id); }
	}

	public static class OrderAlreadyPaidException extends RuntimeException {
		public OrderAlreadyPaidException(long id) { super("Order Already Paid: " + id); }
	}

	public static class PaymentNotFoundException extends RuntimeException {
		public PaymentNotFoundException(long id) { super("Payment Not Found:" + id); }
	}
	
	private record SimulatedResult(boolean succeeded, String failureReason) {};
	private SimulatedResult simulate(String key) {
		String k = key.toLowerCase();
		if (k.endsWith("fail")) return new SimulatedResult(false, "SIMULATED_DECLINE");
		if (k.endsWith("ok")) return new SimulatedResult(true, null);
		int h = stableHash(key);
		boolean success = (Math.floorMod(h, 10) < 7); // 0-6 succeeded (70%), 7-9 fail (30%)
		return success ? new SimulatedResult(true, null) : new SimulatedResult(false, "SIMULATED_DECLINE");
	}

	private int stableHash(String key) {
		byte[] b = key.getBytes(StandardCharsets.UTF_8);
		int h = 0;
		for (byte x: b) h = 31 * h + (x & 0xff);
		return h;
	}
}
