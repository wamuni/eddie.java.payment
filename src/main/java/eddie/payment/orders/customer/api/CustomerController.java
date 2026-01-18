package eddie.payment.orders.customer.api;

import eddie.payment.orders.customer.CustomerRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/v1/customers")
public class CustomerController {
	private final CustomerRepository repo;

	public CustomerController(CustomerRepository repo) {
		this.repo = repo;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CustomerResponse create(@Valid @RequestBody CreateCustomerRequest req) {
		var saved = repo.save(req.email(), req.name());
		return CustomerResponse.from(saved);
	}

	@GetMapping("/{id}")
	public CustomerResponse getById(@PathVariable long id) {
		var customer = repo.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));
		return CustomerResponse.from(customer);
	}
}
