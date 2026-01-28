package eddie.payment.orders.order.api;

import eddie.payment.orders.order.Order;
import eddie.payment.orders.order.OrderItem;
import eddie.payment.orders.order.OrderStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public record OrderResponse(
	long id,
	long customerId,
	OrderStatus status,
	String currency,
	BigDecimal totalAmount,
	OffsetDateTime createdAt,
	List<Item> items
) {
	public record Item(
		long id,
		long productId,
		int quantity,
		BigDecimal unitPriceAmount,
		BigDecimal lineAmount
	) {
		public static Item from(OrderItem i) {
			return new Item(i.id(), i.productId(), i.quantity(), i.unitPriceAmount(), i.lineAmount());
		}
	}

	public static OrderResponse from(Order o) {
		return new OrderResponse(
			o.id(),
			o.customerId(),
			o.status(),
			o.currency(),
			o.totalAmount(),
			o.createdAt(),
			o.orderItems().stream().map(Item::from).toList()
		);
	}
}
