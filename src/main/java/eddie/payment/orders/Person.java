package eddie.payment.orders;

import java.util.Objects;

public class Person {
	String name;
	int age;
	String country;
	public Person() {}
	public Person(String name, int age, String country) {
		this.name = name;
		this.age = age;
		this.country = country;
	}
	
	public String getName() { return this.name; }
	public int getAge() { return this.age; }
	public String getCountry() { return this.country; }
	public String toString() { return "[name: " + this.name + "; age: " + this.age + "; country: " + this.country + ".]"; }

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) { return false; }
		Person person = (Person) o;
		return this.age == person.age && Objects.equals(name, person.name) && Objects.equals(country, person.country);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.name, this.age, this.country);
	}
}
