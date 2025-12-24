package eddie.payment.orders;

public class Printer<T extends Vehicle & Thing> {
	T content;
	Printer(T content) { this.content = content; }
	public void print() { System.out.println(content); }
}
