package messageServer;

import java.util.Scanner;

public class MessageServerCLI {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Willkommen zum Message Broker - Server Interface\n"
						 + "================================================\n");
		System.out.println("...\n Starte Server \n");
		MessageServerIF ms = new MessageServer(55555);
		ms.responseOnMessages();
		scanner.close();
	}

}
