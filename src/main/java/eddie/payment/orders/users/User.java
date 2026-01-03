package eddie.payment.orders;

import java.util.Objects;

public class User {
	String firstName;
	String secondName;

	public User(String firstName, String secondName) {
		this.firstName = firstName;
		this.secondName = secondName;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public String getSecondName() {
		return this.secondName;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		User user = (User) o;
		return Objects.equals(firstName, user.firstName) && Objects.equals(secondName, user.secondName);
	}

	@Override
	public int hashCode() {
		return Objects.hash(firstName, secondName);
	}
}
