package eddie.payment.orders.payment.api;

import jakarta.validation.constraints.NotBlank;

public record CreatePaymentRequest(
	@NotBlank String method
) {};
