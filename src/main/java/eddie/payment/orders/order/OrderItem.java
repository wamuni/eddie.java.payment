package eddie.payment.orders.order;

import java.math.BigDecimal;

public record OrderItem (
	long id,
	long orderId,
	long productId,
	int quantity,
	BigDecimal unitPriceAmount,
	BigDecimal lineAmount
) {};
