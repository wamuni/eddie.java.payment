package eddie.payment.orders;

import java.util.List;
import java.util.ArrayList;

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

		print("hello world");
		print("123");
		print(456);
		print(new Bus("Toyota", "Yellow", "Benz"));

		print("hellow world", 789);
		
		List<String> stringList = new ArrayList<>();
		stringList.add("123");
		stringList.add("234");
		stringList.add("345");
		print(stringList);
		printList(stringList);
		printListWithT(stringList);
		
		List<Bus> CarList = new ArrayList<>();
		CarList.add(new Bus("Toyota", "Yellow", "RR"));
		CarList.add(new Bus("Sabaru", "Blue", "RR"));
		printListWithBound(CarList);
	}

	public static void process(Runnable r) {
		r.run();
	}

	private static <T> void print(T content) {
		System.out.println(content);
	}
	
	private static <T, K> void print(T content, K content2) {
		System.out.println(content);
		System.out.println(content2);
	}
	
	private static void printList(List<?> content) {
		System.out.println(content);
	}

	private static <T> void printListWithT(List<T> content) {
		System.out.println(content);
	}
	
	private static void printListWithBound(List<? extends Vehicle> content) {
		System.out.println(content);
	}
}
