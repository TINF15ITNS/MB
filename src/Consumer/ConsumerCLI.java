package Consumer;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class ConsumerCLI {

	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);
		Consumer user = null;
		while (true) {
			try {
				System.out.print("Bitte geben Sie die Adresse des Servers ein (ohne Port): ");
				user = new Consumer(scanner.nextLine());
				System.out.println("\n");
				break;
			} catch (IOException e) {

				System.out.println("Kein Server unter der angegebenen Adresse erreichbar.");
			}
		}
		scanner.close();
		
		//To be refactored
		user.registerOnServer();
		user.registerOnMulticastGroup();
		user.startAction();

	}
}
