package Consumer;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import MessageServer.Message;
import MessageServer.MessageType;

public class Consumer {
	Scanner scanner;
	private final String name;
	String[] producers;
	public final int portServer;

	public static void main(String[] args) {
		Consumer prod = new Consumer();
	}

	public Consumer() {
		// Prozess der Erzeugung des Konsumenten
		scanner = new Scanner(System.in);
		System.out.print("Name des Konsumenten: ");
		name = scanner.nextLine();

		System.out.println("Bevor Sie Push-Nachrichten für den Konsumenten " + name + " erhalten können, müssen Sie sich erst beim Server registrieren");
		portServer = scanner.nextInt();
		Socket server = null;
		try {
			server = new Socket("localhost", portServer);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		registerOnServer(server);

		producers = getOfferofProducers(server);

		if (server != null) {
			try {
				server.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	public String[] getOfferofProducers(Socket server) {
		// bei Client anfragen nach producer, ist payload null
		// Antwort Message ist im Payload die ganzen ProducerNamen
		Message m = new Message(MessageType.getProducer, null);
		Message answer = sendandGetMessage(m, server);
		// trenne payload nach noch zu spezifizierendem zeichen und stecke es in das array
		// dann ausgabe und auswahl durch User

		return producers;
	}

	// soll true wenn geklappt
	public boolean registerOnServer(Socket server) {
		// das zu verschicken Message-Objekt wird angelegt
		Message m = new Message(MessageType.RegisterOnServer, this.name);
		sendandGetMessage(m, server);
		return true;
	}

	private Message sendandGetMessage(Message m, Socket server) {

		XMLEncoder enc = null;
		XMLDecoder dec = null;

		try {
			enc = new XMLEncoder(server.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		enc.writeObject(m);

		// erhalte Antwort falls erwartet wird
		if (m.getType() != MessageType.RegisterOnServer) {
			try {
				dec = new XMLDecoder(server.getInputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Message answer = (Message) dec.readObject();
			return answer;
		}

		if (enc != null)
			enc.close();
		if (dec != null)
			dec.close();
		return null;
	}

	public boolean deregisterOnServer() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */

}
