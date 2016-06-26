package MessageServer;

import java.util.HashMap;
import java.util.Scanner;

public class MessageServer {

	public final int portMessageServer;
	HashMap<Integer, Object> hashMapProducer, hashMapConsumer;
	Scanner scanner;

	public static void main(String[] args) {
		MessageServer messageServer = new MessageServer();

	}

	public MessageServer() {
		hashMapProducer = new HashMap<>();
		hashMapConsumer = new HashMap<>();

		scanner = new Scanner(System.in);
		System.out.println("Willkommen beim Installationsvorgang Ihres MessageServers:");
		System.out.print("Geben Sie bitte den Port an, über welchen sich Konsumenten und Produzenten den MessageServer erreichen können ");
		portMessageServer = scanner.nextInt();

		System.out.println("\n\n\nIhr MessageServer wurde erfolgreich installiert\nSie erreichen den MessageServer unter folgendem Port: " + portMessageServer);
	}

	// Oder das hier in der MainMethode in eigene threads auslagern
	// da hab ich mir noch keine gedanken drüber gemacht
	/**
	 * waits for Messages
	 */
	public void getMessages() {
		// diese Methode hält den einen Port offen, wo auf nachrichten der konsumenten und Produzenten gehört wird
		// Casted deise dann nachN message und schaut dann was sie damit machen soll und ruft die jeweilige Methode auf
	}

	/**
	 * In this Method the MessageServer offers the available Producers
	 */
	public void offerProducer() {
		// hier ist dann nen ServerSocket, dass nen neuen thread startet, wenn eine Anfrage kommt. In diesem thread wird dann wird dann ne TCP verbindung
		// aufgebaut, um dem Konsumenten die verschiedenen zur verfügung stehenden Produzenten anzubieten
		// wie Skript Folie 37 E
	}

	/**
	 * This Method accepts the register-Messages and saves the Producer and Consuments.
	 */
	public void register() {
		// gleiche Spiel
		// nen ServerSocket wartet auf Anfrage, dass sich ein Produzent oder Konsument registrieren möchte
		// Speichert den Konsument, falls er nen einduetigen Namen hat in der hashMap
	}

	/**
	 * this method forwards the messages of the Producers to the Consumers
	 */
	public void recievePushMessage() {

	}

	public void deregister() {
		// wartet auf Deregistrierunganträge
	}
}
