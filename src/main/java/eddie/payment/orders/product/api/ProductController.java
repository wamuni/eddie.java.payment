package eddie.payment.orders.product.api;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

import eddie.payment.orders.product.ProductRepository;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("v1/products")
public class ProductController {
	
	private final ProductRepository repo;

	public ProductController(ProductRepository repo) {
		this.repo = repo;
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ProductResponse create(@Valid @RequestBody CreateProductRequest req) {
		boolean active = (req.active() == null) ? true: req.active();
		var saved = repo.save(req.sku(), req.name(), req.priceAmount(), req.currency(), active);
		return ProductResponse.from(saved);
	}

	@GetMapping("/{id}")
	public ProductResponse getById(@PathVariable long id) {
		var p = repo.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
		return ProductResponse.from(p);
	}

	@GetMapping
	public List<ProductResponse> list (
		@RequestParam(defaultValue = "20") int limit,
		@RequestParam(defaultValue = "0") int offset
	) {
		int safeLimit = Math.min(Math.max(limit, 1), 100);
		int safeOffset = Math.max(offset, 0);

		return repo.findAll(safeLimit, safeOffset)
			.stream()
			.map(ProductResponse::from)
			.toList();
	}
}
