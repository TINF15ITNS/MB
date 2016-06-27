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
	private Scanner scanner;
	private String name;
	private final int consumerID;
	private String[] producers;
	private final int portServer;

	public static void main(String[] args) {
		// Erzeuge ein Consumer
		Consumer prod = new Consumer();

		// mit dieser Methode startet man die Möglichkeit für den Anwender, iwas zu machen ...
		// bitte erst mal in den Konstruktor schauen
		prod.startAction();

	}

	public Consumer() {
		// Prozess der Erzeugung des Konsumenten !!!
		scanner = new Scanner(System.in);
		System.out.print("Name des Konsumenten: ");
		name = scanner.nextLine();

		System.out.print(
				"Um Nachrichten erhalten zu können, muss dieses Programm :) mit dem Server kommunizieren können. Geben Sie hierfür bitte den Port des Servers an: ");
		portServer = scanner.nextInt();

		// nun werden die Grundeinstellungen erledigt
		consumerID = registerOnServer();

		registerOnProducers();

	}

	public void startAction() {
		boolean exit = true;
		while (exit) {
			System.out.println("Was möchten Sie tun?: ");
			// ...
			System.out.println("Wenn Sie sich für einen neuen Produzenten einschreiben wollen, geben Sie die Option \"p\" ein ");
			System.out.println("Möchten Sie den Konsumenten beenden, geben Sie die Option \"exit\" ein");
			String s = scanner.nextLine();
			switch (s) {
			case "p":
				registerOnProducers();
				break;
			case "exit":
				break;
			default:
			}
		}
	}

	public void registerOnProducers() {
		Socket server = connectionToServer();
		getOfferofProducers(server);
		System.out.println("Sie können sich für die folgenden Produzenten einschreiben: ");
		for (int i = 0; i < producers.length; i++) {
			System.out.print(producers[i] + ", ");
		}
		System.out.print("\nGeben Sie den Namen, der betreffenden Produzenten, für die Sie sich einschreiben wollen, mit einem \", \" getrennt ein: ");
		String[] hilf = scanner.nextLine().split(",");
		String hilf2 = "";
		for (int i = 0; i < hilf.length; i++) {
			hilf2 += hilf[i] + ";";
		}
		// überprufen, dass richtig eingegeben

		// ...

		Message m = new Message(MessageType.RegisterOnProducer, hilf2);
		Message answer = sendandGetMessage(m, server);
		switch (answer.getPayload()) {
		case "ok":
			System.out.println("Der Einschreibevorgang war erfolgreich! Sie werden bei Push-Nachrichten Ihrer abonnierten Produzenten informiert...");
			// dann mache nichts weiter
			break;
		case "cannotRegisterOnProducers":
			// ....
			break;
		default:
		}
		if (server != null) {
			try {
				server.close();
			} catch (IOException e) {
				System.out.println("Socket lässt sich nicht schließen");
				e.printStackTrace();
			}
		}

	}

	public void getOfferofProducers(Socket server) {
		// bei Client anfragen nach producer, ist payload null
		// Antwort Message ist im Payload die ganzen ProducerNamen mit ; getrennt
		// hab ich jetzt mal so definiert
		Message m = new Message(MessageType.getProducer, null);
		Message answer = sendandGetMessage(m, server);

		producers = answer.getPayload().split(";");
	}

	public int registerOnServer() {
		// ich hab jetzt eine Methode erstellt, die ein Socket zurückliefert, welches mit dem Server kommuniziert. Das alles in ner eigenen Methode, da für das
		// Einschreiben auf Produzenten, ne eigener Socket benötigt wird (später, wenn der Konsument registriert wurde und der Anwender sich auf neuen
		// einschreiben will)
		Socket server = connectionToServer();
		// das zu verschicken Message-Objekt wird angelegt
		Message m = new Message(MessageType.RegisterOnServer, this.name);
		// antwort verarbeiten
		Message answer = sendandGetMessage(m, server);
		int hilf = (int) new Integer(answer.getPayload());

		if (server != null) {
			try {
				server.close();
			} catch (IOException e) {
				System.out.println("Socket lässt sich nicht schließen");
				e.printStackTrace();
			}
		}
		return hilf;
	}

	private Socket connectionToServer() {
		Socket s = null;
		try {
			s = new Socket("localhost", portServer);
		} catch (UnknownHostException e) {
			System.out.println("Die Serveradresse stimmtn nicht");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IO-Probleme...");
			e.printStackTrace();
		}
		return s;
	}

	private Message sendandGetMessage(Message m, Socket server) {

		XMLEncoder enc = null;
		XMLDecoder dec = null;
		Message answer = null;
		try {
			enc = new XMLEncoder(server.getOutputStream());

			enc.writeObject(m);

			// erhalte Antwort falls erwartet wird
			dec = new XMLDecoder(server.getInputStream());
			answer = (Message) dec.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (enc != null)
				enc.close();
			if (dec != null)
				dec.close();
		}

		return answer;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

}
