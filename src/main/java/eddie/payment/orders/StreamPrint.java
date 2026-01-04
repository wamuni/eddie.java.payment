package eddie.payment.orders;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Arrays;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.IOException;
import java.util.stream.IntStream;
import java.util.Collection;
import java.util.Comparator;

public class StreamPrint {
	public StreamPrint() {};
	public void StartToPrint() {
		System.out.println("Hello Stream!:");
		List<Person> people = List.of(
			new Person("Neo", 46, "USA"),
			new Person("Stan", 10, "USA"),
			new Person("Stan", 10, "USA"),
			new Person("Alex", 20, "UK"),
			new Person("Grace", 16, "UK"),
			new Person("Grace", 16, "UK"),
			new Person("Alex", 20, "UK"),
			new Person("Sebastian", 40, "FR")
		);
		
		List<List<Person>> peopleGroup = List.of(
			List.of(
				new Person("Neo", 45, "USA"),
				new Person("Stan", 10, "USA")
			),
			List.of(
				new Person("Alex", 15, "UK"),
				new Person("Sebastian", 40, "FR"),
				new Person("Neo", 45, "USA")
			)
		);

		System.out.println("All functions together");

		peopleGroup.stream()
			.flatMap(Collection::stream)
			.filter(person -> person.getAge() > 18)
			.distinct()
			.sorted(Comparator.comparingInt(Person::getAge).reversed())
			.map(Person::getName)
			.limit(3)
			.skip(1)
			.forEach(System.out::println);

		System.out.println("===========================");
		peopleGroup.stream().flatMap(Collection::stream).map(Person::getName).forEach(System.out::println);

		List<Person> adults = new ArrayList<>();
		for (Person person: people) {
			if (person.getAge() > 18) { adults.add(person); }
		}
		System.out.println(adults);
		//distinct will maintaince a hashmap
		Stream.of("apple", "orange", "apple", "orange", "cherry").distinct().forEach(System.out::println);

		System.out.println("Distinct intermediate method called for People");
		people.stream().distinct().forEach(System.out::println);

		// Map data in the stream to another stream
		Stream<Person> peopleStream = people.stream();
		Stream<String> peopleNameStrema = peopleStream.distinct().map(Person::getName);
		peopleNameStrema.forEach(System.out::println);
		
		List<Person> adultsStream = people.stream().filter(person -> person.getAge() > 18).collect(Collectors.toList());
		System.out.println(adultsStream);

		List<String> list = List.of("a", "b", "c");
		Stream<String> stream = list.stream();
		stream.forEach(item -> System.out.println(item));

		String[] array = {"d", "e", "f"};
		Stream<String> arrayStream = Arrays.stream(array);
		arrayStream.forEach(System.out::println);
		
		Stream<Integer> intStream1 = Stream.of(1, 2, 3);
		Stream<Integer> intStream2 = Stream.of(4, 5, 6);
		Stream.concat(intStream1, intStream2).forEach(System.out::println);
		
		Stream.Builder<String> streamBuilder = Stream.builder();
		streamBuilder.add("a");
		streamBuilder.add("b");
		if (Math.random() > 0.5) { streamBuilder.add("c"); }
		Stream<String> streamFromStreamBuilder = streamBuilder.build();
		streamFromStreamBuilder.forEach(System.out::println);
		
		//Handle file with stream
		Path path = Path.of("file.txt");
		System.out.println(Path.of(".").toAbsolutePath());
		try (Stream<String> lines = Files.lines(path)) {
			lines.forEach(System.out::println);
		} catch (IOException e) {
			e.getStackTrace();
		}
		
		IntStream intStream = IntStream.range(1, 5);
		Stream<Integer> integerStreamFromBox = intStream.boxed();
		integerStreamFromBox.forEach(System.out::println);
		
		Stream.generate(Math::random).limit(10).forEach(System.out::println);
		Stream.generate(() -> "Eddie").limit(5).forEach(System.out::println);
		Stream.iterate(0, n -> n <= 10, n -> n + 2).forEach(System.out::println);
		
		// Parallel Stream
		List.of("a", "b", "c").parallelStream().forEach(System.out::println);
		Stream.of("blueberry", "cherry", "apple", "pear").sorted().forEach(System.out::println);
		Stream.of("blueberry", "cherry", "apple", "pear").sorted(Comparator.comparingInt(String::length)).forEach(System.out::println);
	}
}
