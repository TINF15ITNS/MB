/**
 * 
 */
package consumer;

import java.io.IOException;
import java.util.Scanner;

/**
 * 
 * @author Nikolai Seip, Sebastian Mattheis, Fabian Hinz
 *
 */
public class ConsumerCLI {

	private static ConsumerIF user = null;
	private static Scanner scanner;

	public static void main(String[] args) {

		scanner = new Scanner(System.in);
		boolean exit = false;

		System.out.println("Willkommen zum Consumer Command Line Interface\n" + "==============================================\n");

		while (true) {
			try {
				System.out.print("Bitte geben Sie die Adresse des Servers ein (ohne Port): ");
				String addr = scanner.nextLine();
				user = new Consumer(addr);
				System.out.println();
				break;
			} catch (IOException e) {
				System.out.println("Kein Server unter der angegebenen Adresse erreichbar.");
			}
		}

		while (!exit) {
			System.out.println("\nBitte wählen Sie durch Eingabe einer Zahl:");
			if (!user.isRegistered()) {
				System.out.print("(1) Registrierung beim Server\n" + "(2) Liste von Produzenten ansehen\n" + "(3) Beenden der CLI\n" + "Eingabe: ");
				int input;
				try {
					input = scanner.nextInt();
				} catch (Exception e) {
					scanner.nextLine();
					continue;
				}

				scanner.nextLine(); // Absolutely necessary because nextInt() reads only one int and does not finish the line.
				switch (input) {
				case 1:
					if (user.registerOnServer()) {
						System.out.println("Die Registrierung war erfolgreich.");
					} else {
						System.out.println("Die Registrierung ist leider fehlgeschlagen.");
					}
					break;
				case 2:
					String[] producers = user.getProducers().toArray(new String[0]);
					if (producers == null)
						System.out.println("Fehler bei der Abfrage der Produzenten");
					else if (producers.length == 0)
						System.out.println("Es sind momentan keine Produzenten registriert.");
					else {
						System.out.println("Verfügbare Produzenten:");
						for (String p : producers) {
							System.out.println("* " + p + "\n");
						}
					}
					break;
				case 3:
					System.out.println("Beenden...\n");
					exit = true;
					break;
				default:
					System.out.println("Bitte wählen Sie eine der gegebenen Optionen:");
					break;
				}
			} else {
				System.out.print("(1) Abmeldung vom Server\n" + "(2) Liste von Produzenten ansehen\n" + "(3) Abonnieren von Produzenten\n"
						+ "(4) Produzentenabo kündigen\n" + "(5) Abonnements anzeigen\n" + "(6) Erhaltene Nachrichten anzeigen");
				if (user.hasNewMessages()) {
					System.out.print(" (neue Nachrichten vorhanden)");
				}
				System.out.println("\n" + "(7) Beenden der CLI\n" + "Eingabe: ");
				int input;
				try {
					input = scanner.nextInt();
				} catch (Exception e) {
					scanner.nextLine();
					continue;
				}

				scanner.nextLine(); // Absolutely necessary because nextInt() reads only one int and does not finish the line.
				switch (input) {
				case 1:
					if (user.deregisterFromServer()) {
						System.out.println("Die Abmeldung war erfolgreich.");
					} else {
						System.out.println("Die Abmeldung ist leider fehlgeschlagen.");
					}
					break;
				case 2:
					String[] producers = user.getProducers().toArray(new String[0]);
					if (producers == null)
						System.out.println("Fehler bei der Abfrage der Produzenten.");
					else if (producers.length == 0)
						System.out.println("Es sind momentan keine Produzenten registriert.");
					else {
						System.out.println("Verfügbare Produzenten:");
						for (String p : producers) {
							System.out.print("* " + p + "\n");
						}
					}
					break;
				case 3:
					System.out.print("Welche Produzenten möchten Sie abonnieren?\n" + "(Bei mehreren bitte mit Komma trennen.) ");
					String[] failedSubscriptions = user.subscribeToProducers(scanner.nextLine().trim().replaceAll("[ ]*,[ ]*+", ",").split(","));
					if (failedSubscriptions.length > 0) {
						System.out.println("Es war nicht möglich, die folgenden Produzenten zu abonnieren:");
						for (String producer : failedSubscriptions) {
							System.out.println("* " + producer + "\n");
						}
					}
					break;
				case 4:
					System.out.print("Welchen Produzenten soll das Abonnement gekündigt werden?\n" + "(Bei mehreren bitte mit Komma trennen.) ");
					String[] failedUnsubscriptions = user.unsubscribeFromProducers(scanner.nextLine().trim().replaceAll("[ ]*,[ ]*+", ",").split(","));
					if (failedUnsubscriptions.length > 0) {
						System.out.println("Sie konnten folgenden Produzenten nicht kündigen:");
						for (String producer : failedUnsubscriptions) {
							System.out.println("* " + producer + "\n");
						}
					}
					break;
				case 5:
					String[] subscriptions = user.getSubscriptions();
					if (subscriptions.length == 0) {
						System.out.println("Keine Abbonements vorhanden.");
						break;
					}
					System.out.println("\nIhre Abos:");
					for (String s : subscriptions) {
						System.out.println("* " + s);
					}
					break;
				case 6:
					String ausgabe = user.getNewBroadcasts();
					if (!ausgabe.equals("")) {
						System.out.println(ausgabe);
					} else {
						System.out.println("\nSie haben keine neuen Nachrichten");
					}
					break;
				case 7:
					exit = true;
					break;
				default:
					System.out.println("\nBitte wählen Sie eine der gegebenen Optionen.");
					break;
				}
			}
		}

		scanner.close();
		user.stopReceiving();
		user.unsubscribeFromProducers(user.getSubscriptions());
		user.deregisterFromServer();
		System.out.println("Der Consumer wurde beendet");
	}
}
