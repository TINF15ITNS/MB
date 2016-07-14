package producer;

import java.io.IOException;
import java.util.Scanner;

public class ProducerCLI {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		Producer prod = null;
		boolean exit = false;

		System.out.println("Herzlich Willkommen\n\n");

		while (true) {
			try {
				System.out.println("Bitte geben Sie den gewünschten Namen für den Produzenten ein:");
				String n = scanner.nextLine();
				System.out.print("Bitte geben Sie die Adresse des Servers ein (ohne Port): ");
				String addr = scanner.nextLine();
				prod = new Producer(n, addr);
				System.out.println("\n");
				break;
			} catch (IOException e) {
				System.out.println("Kein Server unter der angegebenen Adresse erreichbar.");
			} catch (Exception e) {
				System.out.println("Leider ist dieser Name schon belegt.");
			}
		}

		while (!exit) {
			System.out.println("'1' Anmeldung, '2' Abmeldung, '3' Sende Nachricht, '4' Beenden");

			int input = scanner.nextInt();
			scanner.nextLine(); // Absolutely necessary because nextInt() reads only one int and does not finish the line.
			switch (input) {
			// TODO: Implement confirmation routines and error messages for the user
			case 1:
				prod.registerOnServer();
				break;
			case 2:
				prod.deregisterFromServer();
				break;
			case 3:
				System.out.println("Geben Sie im bitte Ihre neue Nachricht ein und beenden Sie die Eingabe mit nur '#end' in der letzten Zeile");
				StringBuffer m = new StringBuffer();
				while (scanner.hasNext()) {
					String tmp = scanner.nextLine();
					if (tmp.equals("#end"))
						break;
					m.append(tmp);
					m.append(new String("\n"));
				}
				prod.sendMessage(m.toString());
				break;

			case 4:
				exit = true;
				break;
			default:
				break;
			}
		}
		scanner.close();
		prod.deregisterFromServer();
	}

}
