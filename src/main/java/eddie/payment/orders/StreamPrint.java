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
import java.util.Map;
import java.util.IntSummaryStatistics;
import java.util.stream.Collector;
import java.util.HashMap;

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
		
		System.out.println("Data Summary");
		IntSummaryStatistics intSummary = people.stream().collect(Collectors.summarizingInt(Person::getAge));
		System.out.println("Age average: " + intSummary.getAverage());
		System.out.println("Age maximum: " + intSummary.getMax());
		System.out.println("Join name using collect method");
		String joinningName = people.stream().map(Person::getName).collect(Collectors.joining(","));
		System.out.println(joinningName);

		System.out.println("Partition by Age");
		Map<Boolean, List<Person>> peoplePartitioningByAge = people
			.stream()
			.collect(Collectors.partitioningBy(person -> person.getAge() > 18));
		
		peoplePartitioningByAge.forEach((k, v) -> System.out.println(k + "-" + v));
		
		Map<String, List<Person>> peopleGroupByCountry = people.stream().collect(Collectors.groupingBy(Person::getCountry));
		peopleGroupByCountry.forEach((k, v) -> System.out.println(k + "=" + v));
		
		System.out.println("Reduce method to combine Name with comma");
		String nameString = people.stream().map(Person::getName).reduce("", (a, b) -> a + b + ",");
		System.out.println(nameString);

		System.out.println("Reduce method");
		int sum = people.stream().mapToInt(Person::getAge).reduce(0, (a, b) -> a + b);
		System.out.println(sum);

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
		System.out.println("How exactly collect method works");
		List<Person> pppp = people.stream()
			.collect(Collector.of(
				() -> new ArrayList(),
				(l, person) -> {
					System.out.println("This is defined accumulator" + l);
					l.add(person);
				},
				(left, right) -> {
					System.out.println("This is a combinar, provided by supplier");
					left.addAll(right);
					return left;
				},
				Collector.Characteristics.IDENTITY_FINISH	
			));
		System.out.println(pppp);
		
//		Map<String, List<Person>> countryMapUsingCustomized = people.stream()
//			.collect(Collector.of(
//				() -> new HashMap<>(),
//				(m, person) -> {
//					String cty = person.getCountry();
//					if (m.containsKey(cty)) {
//						List newList = m.get(cty);
//						newList.add(person);
//						m.put(cty, newList);
//					} else {
//						m.put(cty, list.of(person));
//					}
//				},
//				(left, right) -> {
//					left.putAll(right);
//					return left;
//				},
//				Collector.Characteristics.IDENTITY_FINISH
//			));
//		countryMapUsingCustomized.forEach((k, v) -> System.out.println(k + "-" + v));
	}
}
