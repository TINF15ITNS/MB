package Consumer;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class ConsumerClient {

	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);

		System.out.print("Um Nachrichten erhalten zu können, muss dieser Client mit dem Server kommunizieren können...");
		boolean correctInetAddress = false;
		InetAddress iadr = null;
		int portServer = 0;

		do {
			System.out.print("Geben Sie bitte die Adresse des Servers an: ");
			iadr = InetAddress.getByName(scanner.nextLine());
			if (!correctInetAddress) correctInetAddress = testConnection(iadr);
		} while (!correctInetAddress);

		// Erzeuge ein Consumer
		Consumer prod = new Consumer(name, iadr, 50555);
		prod.registerOnServer();
		prod.registerOnMulticastGroup();
		prod.registerOnProducers();
		// mit dieser Methode startet man die Möglichkeit für den Anwender, iwas
		// zu machen ...
		prod.startAction();

	}

	private static boolean testConnection(InetAddress adress) 
	{	
			try {
				if (!adress.isReachable(50)) {
					System.out.println("Der Server ist unter der Adresse: " + adress.toString() + " nicht erreichbar");
					return false;
				}
			} catch {
				System.out.println("Der Server ist unter der Adresse: " + adress.toString() + " nicht erreichbar");
				return false;
			}
		return true;
}}
