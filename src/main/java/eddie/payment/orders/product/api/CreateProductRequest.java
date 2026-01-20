package eddie.payment.orders.product.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;

public record CreateProductRequest(
	@NotBlank @Size(max = 64) String sku,
	@NotBlank @Size(max = 255) String name,
	@NotNull @DecimalMin(value = "0.00", inclusive = false) BigDecimal priceAmount,
	@NotBlank @Pattern(regexp = "^[A-Z]{3}$") String currency,
	Boolean active
){};
