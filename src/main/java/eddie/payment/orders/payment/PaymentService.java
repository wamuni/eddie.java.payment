package eddie.payment.orders.payment;

import eddie.payment.orders.order.OrderRepository;
import eddie.payment.orders.order.OrderStatus;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.math.BigDecimal;
@Service
public class PaymentService {

	private final OrderRepository orders;
	private final PaymentRepository payments;

	public PaymentService(OrderRepository orders, PaymentRepository payments) {
		this.orders = orders;
		this.payment = payments;
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
		var header = orders.findHeaderById(orderid).orElseThrow(() -> new OrderMissingException(orderId));

		if (header.status() == OrderStatus.PAID) {
			throw new OrderAlreadyPaidException(orderId);
		}
		BigDecimal amount = header.totalAmount();
		String currency = header.currency();

		// 3) Insert Pending payment;
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
		}
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
