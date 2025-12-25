package eddie.payment.orders;

import java.util.Optional;
public class UserRepository {
	public Optional<User> findUserByName(String name) {
		if (name.equals("Eddie")) {
			return Optional.of(new User("Eddie", "Pan"));
		} else {
			return Optional.empty();
		}
	}
}
