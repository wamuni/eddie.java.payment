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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/v1/customers")
public class CustomerController {
	private final CustomerRepository repo;
	private static final Logger log = LoggerFactory.getLogger(CustomerController.class);

	public CustomerController(CustomerRepository repo) {
		this.repo = repo;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CustomerResponse create(@Valid @RequestBody CreateCustomerRequest req) {
		log.info("Create customer request email = {}", req.email());
		var saved = repo.save(req.email(), req.name());
		return CustomerResponse.from(saved);
	}

	@GetMapping("/{id}")
	public CustomerResponse getById(@PathVariable long id) {
		log.info("Get customer request id = {}", id);
		var customer = repo.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));
		return CustomerResponse.from(customer);
	}

	@PutMapping("/{id}")
	public CustomerResponse update(
		@PathVariable long id,
		@Valid @RequestBody UpdateCustomerRequest req
	) {
		log.info("Update customer request email = {}", req.email());
		var updated = repo.update(id, req.email(), req.name())
			.orElseThrow(() -> new CustomerNotFoundException(id));
		return CustomerResponse.from(updated);
	}

	@GetMapping
	public List<CustomerResponse> list(
		@RequestParam(defaultValue = "20") int limit,
		@RequestParam(defaultValue = "0") int offset
	) {
		log.info("Get customer list request");
		int safeLimit = Math.min(limit, 100);
		int safeOffset = Math.max(offset, 0);

		return repo.findAll(safeLimit, safeOffset)
			.stream()
			.map(CustomerResponse::from)
			.toList();
	}
}
