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
			System.out.println("'1' Anmeldung, '2' Abmeldung, '3' Produzentenliste, '4' Abonnieren, '5' Deabonnieren, '6' Liste der Anmeldungen, '7' Beenden");

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
				System.out.print("Welche Produzenten sollen abonniert werden (mit Kommatas trennen)? ");
				String[] subscriptions = user.subscribeToProducers(scanner.nextLine().replaceAll("\\s+", "").split(","));
				System.out.println("Sie haben folgende Produzenten abonniert:");
				for (String producer : subscriptions) {
					System.out.println("\t" + producer);
				}
				break;
			case 5:
				System.out.print("Welche Produzenten sollen deabonniert werden (mit Kommatas trennen)? ");
				String[] unsubscriptions = user.unsubscribeFromProducers(scanner.nextLine().replaceAll("\\s+", "").split(","));
				System.out.println("Sie haben folgende Produzenten deabonniert:");
				for (String producer : unsubscriptions) {
					System.out.println("\t" + producer);
				}
				break;
			case 6:
				System.out.println("Ihre Abos:");
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
