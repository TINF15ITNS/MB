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

/*
 
  public void startAction() {
		boolean exit = true;
		while (exit) {

			System.out.println("Was m�chten Sie tun?: ");
			// ...
			System.out.println("Wenn Sie sich f�r einen neuen Produzenten einschreiben wollen, geben Sie die Option \"p\" ein ");
			System.out.println("M�chten Sie den Konsumenten beenden, geben Sie die Option \"exit\" ein");
			String s = scanner.nextLine();
			switch (s) {
			case "p":
				registerOnProducers();
				break;
			case "exit":
				exit = false;
				deregister();
				break;
			default:
			}
		}
	}
*/
