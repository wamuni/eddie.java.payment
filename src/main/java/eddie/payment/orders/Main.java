package eddie.payment.orders;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

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
		
		UserRepository userRepository = new UserRepository();
		Optional<User> optionalUser = userRepository.findUserByName("Eddie");
		if  (optionalUser.isPresent()) {
			System.out.println(optionalUser.get().getSecondName());
		} else {
			User defaultUser = new User("Neo", "Handerson");
			System.out.println(defaultUser.getSecondName());
		}
		
		// orElse, this process of initialization will always be executed
		User user = optionalUser.orElse(new User("Neo", "Anderson"));
		System.out.println(user.getSecondName());
		
		// Using orElseGet() with Lambda expression, this will be only executed when null value
		User userFromOrElseGet = optionalUser.orElseGet(() -> new User("Neo", "Anderson II"));
		System.out.println(userFromOrElseGet.getSecondName());

		// Using orElseThrow
		// User useThrow = optionalUser.orElseThrow(() -> new RuntimeException("User not found"));
		
		// ifPresent()
		// public void ifPresent(java.util.function.Consumer<? super T> action)
		// when the user is null, then there won't be anything executed;
		// when the user is exist, then it will execute the function;
		optionalUser.ifPresent(u -> System.out.println(u.getSecondName()));

		// ifPresentOrElse()
		optionalUser.ifPresentOrElse(u -> System.out.println(u.getFirstName()), () -> System.out.println("User not found"));
		
		// Optional.filter()
		Optional<User> ou = optionalUser.filter(u -> u.getSecondName().equals("Pan"));
		System.out.println(ou.isPresent());

		// Optional.map()
		Optional<String> optionalString = optionalUser.map(User::getSecondName);
		optionalString.ifPresentOrElse(
				os -> System.out.println(os),
				() -> System.out.println("No value mapped")
		);

		// Optional Class
		Optional<Object> optionalBox = Optional.empty();
		System.out.println(optionalBox.isPresent());
		System.out.println(optionalBox.isEmpty());

		String value = "Eddie";
		optionalBox = optionalBox.of(value);
		System.out.println(optionalBox.isPresent());
		System.out.println(optionalBox.isEmpty());
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
