package messageServer;

import java.util.HashMap;
import java.util.Scanner;

public class MessageServer {

	// nicht das gleihe Objekt zur�cksenden! Wird iwie die gleiche Referenz zur�ckgesendet, dann erkennt das der Client und nimmt das alte Objekt warum auch
	// immer und nicht das neuen mit den ver�nderten Variablenwerten ... Quelle Internet

	public final int portMessageServer;
	HashMap<Integer, Customer> hashMapProducer, hashMapConsumer;
	Scanner scanner;

	public static void main(String[] args) {
		MessageServer messageServer = new MessageServer();

	}

	public MessageServer() {
		hashMapProducer = new HashMap<>();
		hashMapConsumer = new HashMap<>();

		scanner = new Scanner(System.in);
		System.out.println("Willkommen beim Installationsvorgang Ihres MessageServers:");
		System.out.print("Geben Sie bitte den Port an, �ber welchen Konsumenten und Produzenten den MessageServer erreichen k�nnen ");
		portMessageServer = scanner.nextInt();

		System.out.println("\n\n\nIhr MessageServer wurde erfolgreich installiert\nSie erreichen den MessageServer unter folgendem Port: " + portMessageServer);
	}

	// Oder das hier in der MainMethode in eigene threads auslagern
	// da hab ich mir noch keine gedanken dr�ber gemacht
	/**
	 * waits for Messages
	 */
	public void getMessages() {
		// diese Methode h�lt den einen Port offen, wo auf nachrichten der konsumenten und Produzenten geh�rt wird
		// Casted diese dann nach message und schaut dann was sie damit machen soll und ruft die jeweilige Methode auf

		// hier ist dann nen ServerSocket, dass nen neuen thread startet, wenn eine Anfrage kommt.
		// wie Skript Folie 37 E
	}

	/**
	 * In this Method the MessageServer offers the available Producers
	 */
	public void offerProducer() {

	}

	/**
	 * This Method accepts the register-Messages and saves the Producer and Consuments.
	 */
	public void register() {
		// kommt ne registrierungsanfrage, dann wird hier ne id erzeugt und als Message zur�ckgesendet
		// und es wird ein ConsumerMS Objekt erzeugt ( siehe Interface Customer) und in der HashMap gespeichert
		// Key Value ist die id
	}

	/**
	 * this method forwards the messages of the Producers to the Consumers
	 */
	public void recievePushMessage() {

	}

	/**
	 * 
	 */
	public void deregister() {
	}
}
