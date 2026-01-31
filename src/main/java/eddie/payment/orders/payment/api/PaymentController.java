package eddie.payment.orders.payment.api;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import eddie.payment.orders.payment.PaymentService;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
public class PaymentController {
	private final PaymentService service;
	public PaymentController(PaymentService service) {
		this.service = service;
	}

	@PostMapping("/v1/orders/{orderId}/payments")
	@ResponseStatus(HttpStatus.CREATED)
	public PaymentResponse create(
		@PathVariable long orderId,
		@RequestHeader(name = "Idempotency-Key", required = false) String idempotencyKey,
		@Valid @RequestBody CreatePaymentRequest req
	) {
		var p = service.createPayment(orderId, idempotencyKey, req.method());
		return PaymentResponse.from(p);
	}

	@GetMapping("/v1/payments/{paymentId}")
	public PaymentResponse get(@PathVariable long paymentId) {
		return PaymentResponse.from(service.get(paymentId));
	}

	@GetMapping("/v1/orders/{orderId}/payments")
	public List<PaymentResponse> listForOrder(
		@PathVariable long orderId,
		@RequestParam(defaultValue = "20") int limit,
		@RequestParam(defaultValue = "0") int offset
	) {
		return service.listForOrder(orderId, limit, offset).stream().map(PaymentResponse::from).toList();
	}
}
