package eddie.payment.orders;

public class Main {
	public static void main(String[] args) {
		Printer<Bus> stringprinter = new Printer<>(new Bus("Toyota", "Red", "Benz"));
		stringprinter.print();

		Runnable r1 = () -> System.out.println("Hello World from Runnable One");
		Runnable r2 = new Runnable() {
			public void run() {
				System.out.println("Hellow World from Runnable Two");
			}
		};
		process(r1);
		process(r2);
		process(() -> System.out.println("Hello World from Lambda function"));
	}

	public static void process(Runnable r) {
		r.run();
	}
}
