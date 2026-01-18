package eddie.payment.orders.customer;

import java.util.Optional;
import java.util.List;

public interface CustomerRepository {
	Customer save(String email, String name);
	Optional<Customer> findById(long id);
	List<Customer> findAll(int limit, int offset);
	Optional<Customer> update(long id, String email, String name);
}
