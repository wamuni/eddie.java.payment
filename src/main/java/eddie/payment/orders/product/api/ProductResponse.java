package eddie.payment.orders.product.api;

import eddie.payment.orders.product.Product;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record ProductResponse(
	long id,
	String sku,
	String name,
	BigDecimal priceAmount,
	String currency,
	boolean active,
	OffsetDateTime createdAt
) {
	public static ProductResponse from(Product p) {
		return new ProductResponse(
			p.id(), p.sku(), p.name(), p.priceAmount(), p.currency(), p.active(), p.createdAt()
		);
	}
}
