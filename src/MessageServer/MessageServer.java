package MessageServer;

import java.util.HashMap;
import java.util.Scanner;

public class MessageServer {

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
		// kommt ne registrierungsanfrage, dann wird hier ne id erzeugt und als Message zurückgesendet
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
