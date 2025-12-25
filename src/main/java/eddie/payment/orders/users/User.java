package eddie.payment.orders;

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
}
