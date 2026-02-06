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
import org.springframework.http.HttpStatus;

import java.util.List;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import eddie.payment.orders.payment.PaymentService;
import eddie.payment.orders.common.ErrorResponse;

@Tag(name = "Payments", description = "Pay on order, idempotency, and payment status.")
@RestController
public class PaymentController {
	private final PaymentService service;
	public PaymentController(PaymentService service) {
		this.service = service;
	}

	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "Created"),
		@ApiResponse(responseCode = "400", description = "Bad Request", content=@Content(schema=@Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "404", description = "Not Found", content=@Content(schema=@Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "409", description = "Conflict", content=@Content(schema=@Schema(implementation = ErrorResponse.class))),
	})
	@PostMapping("/v1/orders/{orderId}/payments")
	@ResponseStatus(HttpStatus.CREATED)
	public PaymentResponse create(
		@PathVariable long orderId,
		@Parameter(
			in = ParameterIn.HEADER,
			name = "Idempotency-Key",
			description = "Required. Unique per order. Reusing the same key returns the same payment result.",
			required = true,
			example = "pay-001-ok"
		)
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
