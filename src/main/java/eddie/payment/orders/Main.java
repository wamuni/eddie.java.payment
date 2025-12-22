package eddie.payment.orders;

public class Main {
	public static void main(String[] args) {
		System.out.println("Hello World from Vim and Maven!");
		Printer printer = new Printer(123);
		printer.print();
		Printer StringPrinter = new Printer("Hello Eddie, this is a message from String Ptiner");
		StringPrinter.print();
		
		Printer<String> stringprinter = new Printer<>("Hello World");
		stringprinter.print();
	}
}
