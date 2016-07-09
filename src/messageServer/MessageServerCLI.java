package messageServer;

import java.util.Scanner;

public class MessageServerCLI {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		System.out.println("Ihr Message-Server wird nun gestartet");
		MessageServer ms = new MessageServer(55555);
		System.out.println("\n\n\nIhr MessageServer wurde erfolgreich gestartet und ist nun betriebsbereit");
		scanner.close();
	}

}
