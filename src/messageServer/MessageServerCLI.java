package messageServer;

import java.util.Scanner;

public class MessageServerCLI {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Ihr Message-Server wird nun gestartet");
		MessageServer ms = new MessageServer(55555);
		System.out.println("\nIhr MessageServer wurde erfolgreich gestartet und ist nun betriebsbereit");
		ms.initialisingForwardingMessages();
		scanner.close();
	}

}
