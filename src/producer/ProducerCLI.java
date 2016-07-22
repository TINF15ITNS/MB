/**
 * 
 */
package producer;

import java.io.IOException;
import java.util.Scanner;

/**
 * 
 * @author Nikolai Seip, Sebastian Mattheis, Fabian Hinz
 *
 */
public class ProducerCLI {
	
	private static ProducerIF prod = null;
	private static Scanner scanner;

	public static void main(String[] args) {
		
		scanner = new Scanner(System.in);
		boolean exit = false;

		System.out.println("Willkommen zum Producer Command Line Interface\n"
						 + "==============================================\n");

		while (true) {
			try {
				System.out.println("Bitte geben Sie den Produzentennamen ein:");
				String n = scanner.nextLine().trim();
				System.out.print("Bitte geben Sie die Adresse des Servers ein (ohne Port): ");
				String addr = scanner.nextLine();
				prod = new Producer(n, addr);
				System.out.println("\n");
				break;
			} catch (IOException e) {
				System.out.println("Kein Server unter der angegebenen Adresse erreichbar.");
			} catch (IllegalArgumentException e) {
				System.out.println("Leider ist dieser Name schon belegt, bitte wählen Sie einen anderen.");
			}
		}

		while (!exit) {
			System.out.println("Bitte wählen Sie durch Eingabe einer Zahl:");
			if (!prod.isRegistered()) {
				System.out.println("(1) Registrierung beim Server\n"
								 + "(2) Beenden der CLI");
				int input = scanner.nextInt();
				scanner.nextLine(); // Absolutely necessary because nextInt() reads only one int and does not finish the line.
				switch (input) {
				// TODO: Implement confirmation routines and error messages for the user
				case 1:
					if (prod.registerOnServer()) {
						System.out.println("Die Registrierung war erfolgreich.");
					} else {
						System.out.println("Die Registrierung ist leider fehlgeschlagen.");
					}
					break;
				case 2:
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
								 + "(2) Sende Nachricht\n"
								 + "(3) Beenden der CLI");
				int input = scanner.nextInt();
				scanner.nextLine(); // Absolutely necessary because nextInt() reads only one int and does not finish the line.
				switch (input) {
				// TODO: Implement confirmation routines and error messages for the user
				case 1:
					if (prod.deregisterFromServer()) {
						System.out.println("Die Abmeldung war erfolgreich.");
					} else {
						System.out.println("Die Abmeldung ist leider fehlgeschlagen.");
					}
					break;
				case 2:
					System.out.println("Geben Sie im bitte Ihre neue Nachricht ein.\n"
						    		 + "Eine mehrzeilige Eingabe ist hierbei möglich.\n"
						    		 + "Signalisieren Sie das Ende der Nachricht bitte mit 'EOT' in einer neuen Zeile.");
					StringBuffer m = new StringBuffer();
					while (scanner.hasNext()) {
						String tmp = scanner.nextLine();
						if (tmp.equals("EOT"))
							break;
						m.append(tmp);
						m.append(new String("\n"));
					}
					if (prod.sendMessage(m.toString())) {
						System.out.println("Die Nachricht ist beim Server eingegangen.");
					} else {
						System.out.println("Das Senden der Nachricht ist leider fehlgeschlagen.");
					}
					break;
				case 3:
					System.out.println("Beenden...\n");
					exit = true;
					break;
				default:
					System.out.println("Bitte wählen Sie eine der gegebenen Optionen.");
					break;
				}
			}
		}
		//clean up on exit
		prod.deregisterFromServer();
		scanner.close();
	}
}
