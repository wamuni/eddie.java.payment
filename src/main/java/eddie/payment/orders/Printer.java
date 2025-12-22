package eddie.payment.orders;

public class Printer<T> {
	T content;

	Printer(T content) { this.content = content; }
	public void print() { System.out.println(content); }
}
