package eddie.payment.orders.order;

import eddie.payment.orders.customer.CustomerRepository;
import eddie.payment.orders.product.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
	
	private final CustomerRepository customers;
	private final ProductRepository products;
	private final OrderRepository orders;
	private final OrderItemRepository orderItems;

	public OrderService(
		CustomerRepository customers,
		ProductRepository products,
		OrderRepository orders,
		OrderItemRepository orderItems
	) {
		this.customers = customers;
		this.products = products;
		this.orders = orders;
		this.orderItems = orderItems;
	}

	@Transactional
	public Order createOrder(long customerId, String currency, List<CreateItem> items) {

		customers.findById(customerId).orElseThrow(() -> new CustomerMissingException(customerId));

		List<ComputedItem> computed = new ArrayList<>();
		BigDecimal total = BigDecimal.ZERO;

		for (CreateItem item: items) {
			if (item.quantity() <= 0) throw new IllegalArgumentException("quantity must be > 0");

			var product = products.findById(item.productId()).orElseThrow(() -> new ProductMissingException(item.productId()));

			if (!product.currency().equals(currency)) {
				throw new IllegalArgumentException("currency mismatch for productId = " + item.productId());
			}

			BigDecimal line = product.priceAmount().multiply(BigDecimal.valueOf(item.quantity()));
			computed.add(new ComputedItem(item.productId(), item.quantity(), product.priceAmount(), line));
			total = total.add(line);
		}

		var header = orders.insert(customerId, OrderStatus.CREATED, currency, total);

		List<OrderItem> savedItems = new ArrayList<>();
		for (ComputedItem ci: computed) {
			savedItems.add(orderItems.insert(header.id(), ci.productId(), ci.quantity(), ci.unitPriceAmount(), ci.lineAmount()));
		}
		
		return new Order(
			header.id(),
			header.customerId(),
			header.status(),
			header.currency(),
			header.totalAmount(),
			header.createdAt(),
			savedItems
		);

	}

	public Order getOrder(long orderId) {
		var header = orders.findHeaderById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
		var items = orderItems.findByOrderId(orderId);
		return new Order(
			header.id(),
			header.customerId(),
			header.status(),
			header.currency(),
			header.totalAmount(),
			header.createdAt(),
			items
		);
	}

	public record CreateItem(long productId, int quantity) {};
	public record ComputedItem(long productId, int quantity, BigDecimal unitPriceAmount, BigDecimal lineAmount) {};
	
	public static class OrderNotFoundException extends RuntimeException {
		public OrderNotFoundException(long id) { super("Order not found: " + id); }
	}
	public static class CustomerMissingException extends RuntimeException {
		public CustomerMissingException(long id) { super("Customer not found: " + id); }
	}
	public static class ProductMissingException extends RuntimeException {
		public ProductMissingException(long id) { super("Product not found: " + id); }
	}
}
