package eddie.payment.orders.customer;

import java.util.Optional;

public interface CustomerRepository {
	Customer save(String email, String name);
	Optional<Customer> findById(long id);
}
