package eddie.payment.orders;

public class Vehicle {
	String brand;
	String color;

	public Vehicle(String brand, String color) {
		this.brand = brand;
		this.color = color;
	}

	public String getBrand() {
		return this.brand;
	}

	public String getColor() {
		return this.color;
	}
}
