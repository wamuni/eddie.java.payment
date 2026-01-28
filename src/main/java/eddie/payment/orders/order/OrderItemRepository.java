package eddie.payment.orders.order;

import java.math.BigDecimal;
import java.util.List;

public interface OrderItemRepository {
	OrderItem insert(long orderId, long productId, int quantity, BigDecimal unitPriceAmount, BigDecimal lineAmount);
	List<OrderItem> findByOrderId(long orderId);
}
