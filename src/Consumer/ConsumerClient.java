package Consumer;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class ConsumerClient {
	private static int serverPort = 55555;
	
	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);

		System.out.print("Um Nachrichten erhalten zu können, muss dieser Client mit dem Server kommunizieren können...");
		boolean correctInetAddress = false;
		String address = null;		

		do {
			System.out.print("Geben Sie bitte die Adresse des Servers an: ");
			address = scanner.nextLine();
			if (!correctInetAddress)
				correctInetAddress = testConnection(address);
		} while (!correctInetAddress);

		// Erzeuge ein Consumer
		Consumer user = new Consumer(InetAddress.getByName(address), serverPort);
		user.registerOnServer();
		user.registerOnMulticastGroup();
		user.startAction();

	}

	private static boolean testConnection(String adress) 
	{	
			try {
				if (!InetAddress.getByName(adress).isReachable(50)) {
					System.out.println("Der Server ist unter der Adresse: " + adress.toString() + " nicht erreichbar");
					return false;
				}
			} catch (Exception e) {
				System.out.println("Der Server ist unter der Adresse: " + adress.toString() + " nicht erreichbar");
				return false;
			}
		return true;
	}
}
