package consumer;

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
			scanner.nextLine(); // Absolutely necessary because nextInt() reads only one int and does not finish the line.
			switch (input) {
			case 1:
				user.registerOnServer();
				user.registerOnMulticastGroup();
				break;
			case 2:
				user.deregisterFromServer();
				break;
			case 3:
				System.out.println("VerfÃ¼gbare Produzenten:");
				for (String p : user.getProducers()) {
					System.out.println("\t" + p);
				}
				break;
			case 4:
				System.out.print("Welche Produzenten sollen abonniert werden (mit Kommatas trennen)? ");
				String[] s1 = user.subscribeToProducers(scanner.nextLine().replaceAll("\\s+", "").split(","));
				if (s1[0] != null)
					System.out.println("Es war nicht möglich, sich für die folgenden Produzenten zu abonnieren: ");
				for (String producer : s1) {
					System.out.println("\t" + producer);
				}
				break;
			case 5:
				System.out.print("Welche Produzenten sollen deabonniert werden (mit Kommatas trennen)? ");
				String[] s2 = user.unsubscribeFromProducers(scanner.nextLine().replaceAll("\\s+", "").split(","));
				if (s2[0] != null)
					System.out.println("Sie konnten sich nicht für die folgenden Produzenten deabonnieren: ");
				for (String producer : s2) {
					System.out.println("\t" + producer);
				}
				break;
			case 6:
				System.out.println("Ihre Abos:");
				for (String s3 : user.getSubscriptions()) {
					System.out.println("\t" + s3);
				}
				break;
			case 7:
				// ich habe jetzt den neuen Thread nicht iwie beendet ... ich gehe mal davon aus, da es auch einObjekt ist, wird es / er gelöscht, wenn nichts
				// mehr auf ihn referenziert
				exit = true;
				break;
			default:
				break;
			}
		}

		scanner.close();
		user.unsubscribeFromProducers(user.getSubscriptions());
		user.deregisterFromServer();
		user.deregisterFromMulticastGroup();
	}
}
