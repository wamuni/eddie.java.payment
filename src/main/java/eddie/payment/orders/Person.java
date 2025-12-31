package eddie.payment.orders;

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
}
