package eddie.payment.orders.product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductRepository {
	Product save(String sku, String name, BigDecimal priceAmount, String currency, boolean active);
	Optional<Product> findById(long id);
	List<Product> findAll(int limit, int offset);
}
