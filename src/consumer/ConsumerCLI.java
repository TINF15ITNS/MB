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

		System.out.println("Willkommen zum Consumer Command Line Interface\n"
						 + "==============================================\n");

		while (true) {
			try {
				System.out.print("Bitte geben Sie die Adresse des Servers ein (ohne Port): ");
				String addr = scanner.nextLine();
				user = new Consumer(addr);
				System.out.println("\n");
				break;
			} catch (IOException e) {
				System.out.println("Kein Server unter der angegebenen Adresse erreichbar.");
			}
		}

		while (!exit) {
			System.out.println("Bitte wählen Sie durch Eingabe einer Zahl:");
			if (!user.isRegistered()) {
				System.out.println("(1) Registrierung beim Server\n"
								 + "(2) Liste von Produzenten ansehen\n"
								 + "(3) Beenden der CLI");
				int input = scanner.nextInt();
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
					String[] producers = user.getProducers();
					if (producers == null)
						System.out.println("Fehler bei der Abfrage der Produzenten");
					else if (producers.length == 0)
						System.out.println("Es sind momentan keine Produzenten registriert.");
					else {
						System.out.println("Verfügbare Produzenten:");
						for (String p : producers) {
							System.out.println("\t" + p);
						}
					}
					break;
				case 3:
					// ich habe jetzt den neuen Thread nicht iwie beendet ... ich gehe mal davon aus, da es auch ein Objekt ist, wird er gelöscht, wenn nichts
					// mehr auf ihn referenziert
					System.out.println("Beenden...\n");
					exit = true;
					break;
				default:
					System.out.println("Bitte wählen Sie eine der gegebenen Optionen.");
					break;
				}
			}
			else {
				System.out.println("(1) Abmeldung vom Server\n"
								 + "(2) Liste von Produzenten ansehen\n"
								 + "(3) Abonnieren von Produzenten\n"
								 + "(4) Produzentenabo kündigen\n"
								 + "(5) Abonnements anzeigen\n"
								 + "(6) Beenden der CLI");
				int input = scanner.nextInt();
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
					String[] producers = user.getProducers();
					if (producers == null)
						System.out.println("Fehler bei der Abfrage der Produzenten.");
					else if (producers.length == 0)
						System.out.println("Es sind momentan keine Produzenten registriert.");
					else {
						System.out.println("Verfügbare Produzenten:");
						for (String p : producers) {
							System.out.println("\t" + p);
						}
					}
					break;
				case 3:
					System.out.print("Welche Produzenten (bitte mit Kommatas trennen) sollen abonniert werden? ");
					String[] failedSubscriptions = user.subscribeToProducers(scanner.nextLine().replaceAll("\\s+", "").split(",")); //TODO Passt die Eingabe hier wirklich?
					if (failedSubscriptions.length > 0) {
						System.out.println("Es war nicht möglich, sich für die folgenden Produzenten zu abonnieren:");
						for (String producer : failedSubscriptions) {
							System.out.println("\t" + producer);
						}
					}
					break;
				case 4:
					System.out.print("Welche Produzenten (bitte mit Kommatas trennen) sollen deabonniert werden? ");
					String[] failedUnsubscriptions = user.unsubscribeFromProducers(scanner.nextLine().replaceAll("\\s+", "").split(",")); //TODO Passt die Eingabe hier wirklich?
					if (failedUnsubscriptions.length > 0) {
						System.out.println("Sie konnten sich nicht für die folgenden Produzenten deabonnieren:");
						for (String producer : failedUnsubscriptions) {
							System.out.println("\t" + producer);
						}
					}
					break;
				case 5:
					System.out.println("Ihre Abos:");
					for (String s3 : user.getSubscriptions()) {
						System.out.println("\t" + s3);
					}
					break;
				case 6:
					// ich habe jetzt den neuen Thread nicht iwie beendet ... ich gehe mal davon aus, da es auch ein Objekt ist, wird er gelöscht, wenn nichts
					// mehr auf ihn referenziert
					System.out.println("Beenden...\n");
					exit = true;
					break;
				default:
					System.out.println("Bitte wählen Sie eine der gegebenen Optionen.");
					break;
				}
			}
		}
		scanner.close();
		// ist das hier notwendig? doch eigentlich nicht in dieser Variante
		user.unsubscribeFromProducers(user.getSubscriptions());
		user.deregisterFromServer();
	}
}
