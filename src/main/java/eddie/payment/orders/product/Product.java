package eddie.payment.orders.product;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record Product(
	long id,
	String sku,
	String name,
	BigDecimal priceAmount,
	String currency,
	boolean active,
	OffsetDateTime createdAt
) {};
