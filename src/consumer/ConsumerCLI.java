/**
 * 
 */
package consumer;

import java.io.IOException;
import java.util.Scanner;

/**
 * A command line interface that interacts with all kinds of classes implementing the interface ConsumerIF.
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
				String address = scanner.nextLine();
				user = new Consumer(address);
				System.out.println();
				break;
			} catch (IOException e) {
				System.out.println("Kein Server unter der angegebenen Adresse erreichbar.");
			}
		}

		while (!exit) {
			System.out.println("\nBitte wählen Sie durch Eingabe einer Zahl:");
			if (!user.isRegistered()) {
				// Show just the options possible without being registered
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
				case 1: // Register on server
					if (user.registerOnServer()) {
						System.out.println("Die Registrierung war erfolgreich.");
					} else {
						System.out.println("Die Registrierung ist leider fehlgeschlagen.");
					}
					break;
				case 2: // List of producers
					String[] producers = user.getProducers().toArray(new String[0]);
					if (producers == null) // null => getProducers() unsuccessful
						System.out.println("Fehler bei der Abfrage der Produzenten");
					else if (producers.length == 0) // no producers available
						System.out.println("Es sind momentan keine Produzenten registriert.");
					else {
						// List all of the available producers
						System.out.println("Verfügbare Produzenten:");
						for (String producer : producers) {
							System.out.println("* " + producer + "\n");
						}
					}
					break;
				case 3: // Exit
					System.out.println("Beenden...\n");
					exit = true;
					break;
				default:
					System.out.println("Bitte wählen Sie eine der gegebenen Optionen:");
					break;
				}
			} else {
				// Shows just the options possible while being registered
				System.out.print("(1) Abmeldung vom Server\n" + "(2) Liste von Produzenten ansehen\n" + "(3) Abonnieren von Produzenten\n"
						+ "(4) Produzentenabo kündigen\n" + "(5) Abonnements anzeigen\n" + "(6) Erhaltene Nachrichten anzeigen");
				if (user.hasNewMessages()) { // If there are messages available it will be shown next to the option tho show them
					System.out.print(" (neue Nachrichten vorhanden)");
				}
				System.out.println("\n" + "(7) Beenden der CLI\n" + "Eingabe: ");

				int input;
				try {
					input = scanner.nextInt(); // Read the input
				} catch (Exception e) {
					scanner.nextLine();
					continue;
				}

				scanner.nextLine(); // Absolutely necessary because nextInt() reads only one int and does not finish the line.
				switch (input) {
				case 1: // Deregistration from server
					if (user.deregisterFromServer()) {
						System.out.println("Die Abmeldung war erfolgreich.");
					} else {
						System.out.println("Die Abmeldung ist leider fehlgeschlagen.");
					}
					break;
				case 2: // List of producers
					String[] producers = user.getProducers().toArray(new String[0]);
					if (producers == null) // null => getProducers() unsuccessful
						System.out.println("Fehler bei der Abfrage der Produzenten.");
					else if (producers.length == 0) // no producers available
						System.out.println("Es sind momentan keine Produzenten registriert.");
					else {
						// List all of the available producers
						System.out.println("Verfügbare Produzenten:");
						for (String producer : producers) {
							System.out.print("* " + producer + "\n");
						}
					}
					break;
				case 3: // Subscribe to producers
					System.out.print("Welche Produzenten möchten Sie abonnieren?\n" + "(Bei mehreren bitte mit Komma trennen.) ");
					String[] failedSubscriptions = user.subscribeToProducers(scanner.nextLine().trim().replaceAll("[ ]*,[ ]*+", ",").split(",")); // Regex
																																					// replaces
																																					// commas
																																					// and all
																																					// whitespaces
																																					// next to
																																					// them with
																																					// just a
																																					// comma
					if (failedSubscriptions.length > 0) {
						// Print all of the failed subscriptions
						System.out.println("Es war nicht möglich, die folgenden Produzenten zu abonnieren:");
						for (String producer : failedSubscriptions) {
							System.out.println("* " + producer + "\n");
						}
					}
					break;
				case 4: // Unsubscribe from producers
					System.out.print("Welchen Produzenten soll das Abonnement gekündigt werden?\n" + "(Bei mehreren bitte mit Komma trennen.) ");
					String[] failedUnsubscriptions = user.unsubscribeFromProducers(scanner.nextLine().trim().replaceAll("[ ]*,[ ]*+", ",").split(",")); // Regex
																																						// replaces
																																						// commas
																																						// and
																																						// all
																																						// whitespaces
																																						// next
																																						// to
																																						// them
																																						// with
																																						// just
																																						// a
																																						// comma
					if (failedUnsubscriptions.length > 0) {
						// Print all of the failed unsubscriptions
						System.out.println("Sie konnten folgenden Produzenten nicht kündigen:");
						for (String producer : failedUnsubscriptions) {
							System.out.println("* " + producer + "\n");
						}
					}
					break;
				case 5: // List of subscriptions
					String[] subscriptions = user.getSubscriptions();
					if (subscriptions.length == 0) {
						System.out.println("Keine Abonnements vorhanden.");
						break;
					}
					System.out.println("\nIhre Abos:");
					for (String subscription : subscriptions) {
						System.out.println("* " + subscription);
					}
					break;
				case 6: // Show recieved messages
					String newBroadcasts = user.getNewBroadcasts(); // All new broadcasts are in a single string
					if (!newBroadcasts.equals("")) {
						System.out.println("Sie haben foglende neue Nachrichten: ");
						System.out.println(newBroadcasts);
					} else {
						System.out.println("\nSie haben keine neuen Nachrichten");
					}
					break;
				case 7: // Exit
					exit = true;
					break;
				default:
					System.out.println("\nBitte wählen Sie eine der gegebenen Optionen.");
					break;
				}
			}
		}

		// Clean up after exit
		scanner.close();
		user.stopReceiving();
		user.unsubscribeFromProducers(user.getSubscriptions());
		user.deregisterFromServer();
		System.out.println("Der Consumer wurde beendet");
	}
}
