package eddie.payment.orders.order.api;

import eddie.payment.orders.order.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("v1/orders")
public class OrderController {
	
	private final OrderService service;

	public OrderController(OrderService service) {
		this.service = service;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public OrderResponse create(@Valid @RequestBody CreateOrderRequest req) {
		var order = service.createOrder(
			req.customerId(),
			req.currency(),
			req.items().stream()
			   .map(i -> new OrderService.CreateItem(i.productId(), i.quantity()))
			   .toList()
		);
		return OrderResponse.from(order);
	}

	@GetMapping("/{id}")
	public OrderResponse get(@PathVariable long id) {
		return OrderResponse.from(service.getOrder(id));
	}
}
