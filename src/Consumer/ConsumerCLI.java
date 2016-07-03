package Consumer;

import java.io.IOException;
import java.util.Scanner;

public class ConsumerCLI {

	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);
		Consumer user = null;
		boolean exit = false;

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

		while (!exit) {
			System.out.println(
					"'1' Anmeldung, '2' Abmeldung, '3' Produzentenliste, '4' Abbonieren, '5' Deabbonieren, '6' Liste der Anmeldungen, '7' Beenden");

			int input = scanner.nextInt();
			scanner.nextLine(); //Absolutely necessary because nextInt() reads only one int and does not finish the line.
			switch (input) {
			case 1:
				user.registerOnServer();
				break;
			case 2:
				user.deregisterFromServer();
				break;
			case 3:
				System.out.println("Verfügbare Produzenten:");
				for (String p : user.getProducers()) {
					System.out.println("\t" + p);
				}
				break;
			case 4:
				System.out.print("Welche Produzenten sollen abboniert werden (mit Kommatas trennen)? ");
				user.subscribeToProducers(scanner.nextLine().replaceAll("\\s+", "").split(","));
				break;
			case 5:
				System.out.print("Welche Produzenten sollen deabboniert werden (mit Kommatas trennen)? ");
				user.unsubscribeFromProducers(scanner.nextLine().replaceAll("\\s+", "").split(","));
				break;
			case 6:
				System.out.println("Ihre Anmeldungen:");
				for (String s : user.getSubscriptions()) {
					System.out.println("\t" + s);
				}
				break;
			case 7:
				exit = true;
				break;
			default:
				break;
			}
		}
		
		scanner.close();
		user.unsubscribeFromProducers(user.getSubscriptions());
		user.deregisterFromServer();
	}
}

