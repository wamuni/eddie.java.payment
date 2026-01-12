package eddie.payment.orders;

import java.util.ArrayList;
import java.util.List;

public class ArrayListPractice {
	public ArrayListPractice() {};
	public void ArrayListPracticeInstance() {
		System.out.println("Check Array basic");
		ArrayList<String> cars = new ArrayList<String>();
		cars.add("Volvo");
		cars.add("BMW");
		cars.add("Ford");
		cars.add("Mazda");
		System.out.println(cars);

		cars.add(0, "Tesla");
		System.out.println(cars);
		System.out.println(cars.get(0));

		cars.set(0, "Toyato");
		System.out.println(cars);
		
		cars.remove(1);
		System.out.println(cars);

		System.out.println("Cars ArrayList Size: " + cars.size());
		
		ArrayList<String> arrayWithNewConstructor = new ArrayList(List.of("1", "2", "3"));
		System.out.println(arrayWithNewConstructor);
	}
}
