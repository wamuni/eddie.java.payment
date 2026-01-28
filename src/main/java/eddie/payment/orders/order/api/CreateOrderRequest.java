package eddie.payment.orders.order.api;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public record CreateOrderRequest(
	@NotNull Long customerId,
	@NotBlank @Pattern(regexp = "^[A-Z]{3}$") String currency,
	@NotNull @Size(min = 1) List<Item> items
) {
	public record Item(
		@NotNull Long productId,
		@NotNull @Min(1) Integer quantity
	) {}
}
