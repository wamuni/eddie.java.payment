package eddie.payment.orders;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Arrays;

public class StreamPrint {
	public StreamPrint() {};
	public void StartToPrint() {
		System.out.println("Hello Stream!:");
		List<Person> people = List.of(
			new Person("Neo", 46, "USA"),
			new Person("Stan", 10, "USA"),
			new Person("Grace", 16, "UK"),
			new Person("Alex", 20, "UK"),
			new Person("Sebastian", 40, "FR")
		);

		List<Person> adults = new ArrayList<>();
		for (Person person: people) {
			if (person.getAge() > 18) { adults.add(person); }
		}
		System.out.println(adults);
		
		List<Person> adultsStream = people.stream().filter(person -> person.getAge() > 18).collect(Collectors.toList());
		System.out.println(adultsStream);

		List<String> list = List.of("a", "b", "c");
		Stream<String> stream = list.stream();
		stream.forEach(item -> System.out.println(item));

		String[] array = {"d", "e", "f"};
		Stream<String> arrayStream = Arrays.stream(array);
		arrayStream.forEach(System.out::println);
	}
}
