package consumer;

import java.io.IOException;
import java.util.Scanner;

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
			scanner.nextLine(); // Absolutely necessary because nextInt() reads only one int and does not finish the line.

			switch (input) {
			case 1:
				if (user.isRegistered()) {
					System.out.println("Sie sind bereits angemeldet.");
					break;
				}
				if (user.registerOnServer()) {
					System.out.println("Der Registrierungprozess war erfolgreich");
				} else {
					System.out.println("Der Registrierungprozess war leider nicht erfolgreich");
				}
				break;

			case 2:
				if (!user.isRegistered()) {
					System.out.println("Sie sind bereits abgemeldet.");
					break;
				}
				user.deregisterFromServer();
				break;

			case 3:
				String[] producers = user.getProducers();

				if (producers == null)
					System.out.println("Felhler bei der Abfrage der Produzenten");
				else if (producers.length == 0)
					System.out.println("Keine Produzenten verfügbar.");
				else {
					System.out.println("Verfügbare Produzenten:");
					for (String p : producers) {
						System.out.println("\t" + p);
					}
				}
				break;

			case 4:
				if (!user.isRegistered()) {
					System.out.println("Sie müssen sich erst anmelden.");
					break;
				}
				System.out.print("Welche Produzenten (bitte mit Kommatas trennen) sollen abonniert werden? ");
				String[] failedSubscriptions = user.subscribeToProducers(scanner.nextLine().replaceAll("\\s+", "").split(",")); //TODO Passt die Eingabe hier wirklich?
				if (failedSubscriptions.length > 0) {
					System.out.println("Es war nicht möglich, sich für die folgenden Produzenten zu abonnieren:");
					for (String producer : failedSubscriptions) {
						System.out.println("\t" + producer);
					}
				}
				break;

			case 5:
				if (!user.isRegistered()) {
					System.out.println("Sie müssen sich erst anmelden.");
					break;
				}
				System.out.print("Welche Produzenten (bitte mit Kommatas trennen) sollen deabonniert werden? ");
				String[] failedUnsubscriptions = user.unsubscribeFromProducers(scanner.nextLine().replaceAll("\\s+", "").split(",")); //TODO Passt die Eingabe hier wirklich?
				if (failedUnsubscriptions.length > 0) {
					System.out.println("Sie konnten sich nicht für die folgenden Produzenten deabonnieren:");
					for (String producer : failedUnsubscriptions) {
						System.out.println("\t" + producer);
					}
				}
				break;

			case 6:
				if (!user.isRegistered()) {
					System.out.println("Sie müssen sich erst anmelden.");
					break;
				}
				System.out.println("Ihre Abos:");
				for (String s3 : user.getSubscriptions()) {
					System.out.println("\t" + s3);
				}
				break;

			case 7:
				// ich habe jetzt den neuen Thread nicht iwie beendet ... ich gehe mal davon aus, da es auch ein Objekt ist, wird er gelöscht, wenn nichts
				// mehr auf ihn referenziert
				exit = true;
				break;
			default:
				break;
			}
		}

		scanner.close();
		// ist das hier notwendig? doch eigentlich nicht in dieser Variante
		user.unsubscribeFromProducers(user.getSubscriptions());
		user.deregisterFromServer();
	}
}
