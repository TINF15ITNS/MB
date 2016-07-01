package Consumer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ConsumerClient {

	public static void main(String[] args) {

		// Prozess der Erzeugung des Konsumenten !!!

		// Das könnte ich eigentlich auch weg lassen, da das ursprünglich für die Benutzeroberfläche gedacht war...
		Scanner scanner = new Scanner(System.in);
		System.out.print("Bitte geben Sie Ihren Namen an: ");
		String name = scanner.nextLine();

		System.out.print("Um Nachrichten erhalten zu können, muss dieser Client mit dem Server kommunizieren können...");
		boolean correctServerSocketAddress = false;
		boolean correctInetAddress = false;
		InetAddress iadr = null;
		int portServer = 0;

		do {
			if (!correctInetAddress) {
				correctInetAddress = true;
				System.out.print("Geben Sie bitte die Adresse des Servers an (-> localhost): ");
				String serverAddress = scanner.nextLine();
				try {
					iadr = InetAddress.getByName(serverAddress);
					if (!iadr.isReachable(50)) {
						System.out.println("Der Server ist unter der Adresse: " + serverAddress + " nicht erreichbar");
						correctInetAddress = false;
					}
				} catch (UnknownHostException e) {
					System.out.println("Der Server ist unter der Adresse: " + serverAddress + " nicht erreichbar");
					correctInetAddress = false;
					continue;
				} catch (IOException e) {
					System.out.println("IOFehler beim Testen, ob die InetAddresse stimmt");
					e.printStackTrace();
				}
			}
			correctServerSocketAddress = true;
			System.out.print("Geben Sie bitte den Port, auf welchem der Server erreichbar ist, an: ");
			portServer = scanner.nextInt();
			SocketAddress socketadr = new InetSocketAddress(iadr, portServer);
			try (Socket testConnectionSocket = new Socket()) {
				testConnectionSocket.connect(socketadr, 100);
			} catch (Exception e) {
				correctServerSocketAddress = false;
				System.out.println("Die Serveradresse stimmt, aber über den angegebenen Port kann keine Verbindung hergestellt werden ...");
				continue;
			}
		} while (!correctServerSocketAddress);

		// kann der Server auchn in der Registirierungsmessageantwort zurückschicken ...
		/*
		 * System.out.print("Zudem geben Sie bitte an über welche Multicastadresse der Server die Nachrichten verschickt: ");
		 * 
		 * InetAddress multicastAddress = null; boolean correctInetAdress = false; while (!correctInetAdress) { correctInetAdress = true; if (multicastAddress
		 * == null) { System.out.println(
		 * "Die Eingabe der Multicastadresse war fehlerhaft ... Bitte schauen Sie noch mal auf Ihrem Server nach und geben Sie sie erneut ein: "); } try {
		 * multicastAddress = InetAddress.getByName(scanner.nextLine()); if (multicastAddress.isReachable(10) == false) { correctInetAdress = false; } } catch
		 * (UnknownHostException e) { correctInetAdress = false; e.printStackTrace(); } catch (IOException e) { System.out.println(
		 * "IOFehler beim testen der eingegebenen Adresse"); }
		 * 
		 * }
		 */

		// Erzeuge ein Consumer
		Consumer prod = new Consumer(name, iadr, portServer);
		prod.registerOnServer();
		prod.registerOnMulticastGroup();
		prod.registerOnProducers();
		// mit dieser Methode startet man die Möglichkeit für den Anwender, iwas zu machen ...
		prod.startAction();

	}

}
