package eddie.payment.orders;

public class Bus extends Car implements Thing {
	String name;
	public Bus(String brand, String color, String name) {
		super(brand, color);
		this.name = name;
	}

	public String getName() { return "City Bus"; }
	
	@Override
	public String toString() { return "Brand: " + this.brand + "; Color: " + this.color; }
}
