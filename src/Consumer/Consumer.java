package Consumer;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import Message.*;

public class Consumer {
	private static int serverPort = 55555;
	private int consumerID;
	private InetAddress serverAddress;
	private InetAddress multicastAddress;
	private Scanner scanner;

	public Consumer(String address) throws IOException {
		this.serverAddress = InetAddress.getByName(address);
		if (!testConnection(serverAddress, 1000))
			throw new IOException("There ist no server on the specified address");
		scanner = new Scanner(System.in);
	}

	/**
	 * Checks if it is possible to establish a TCP connection using the
	 * "serverPort"
	 * 
	 * @param adress
	 *            The address of the server to be checked
	 * @return if the connection was successful
	 */
	private boolean testConnection(InetAddress adress, int timeout) {
		Socket server = new Socket();
		try {
			server.connect(new InetSocketAddress(adress, serverPort), timeout);
			server.close();
			return true;
		} catch (IOException e) {
			return false;
		}

	}

	/**
	 * Fetches a list of all producers this consumer is subscribed to
	 * 
	 * @return a list of producers this consumer is subscribed to
	 */
	public String[] getSubscriptions() {

			Message response = this.sendAndGetMessage(
					new Message(MessageType.getSubscriptions, new PayloadSubscriptions(null)), serverAddress);
			return ((PayloadSubscriptions) response.getPayload()).getSubscriptions();

	}

	/**
	 * 
	 * @param name
	 *            The name of the producer (can not contain whitespaces)
	 * @return
	 */
	public boolean subscribeToProducers(String[] name) {
		Socket server = getTCPConnectionToServer();
		String[] producers = getOfferofProducers(server);
		System.out.println("Sie k�nnen sich f�r die folgenden Produzenten einschreiben:");
		for (int i = 0; i < producers.length; i++) {
			System.out.print(producers[i] + " , ");
		}

		System.out.print(
				"\nGeben Sie den Namen, der betreffenden Produzenten, f�r die Sie sich einschreiben wollen, mit einem \",\" getrennt ein: ");
		String[] input = scanner.nextLine().split(",");

		// Durch die Eingabe des User entsteht bei Mehrfachauswahl hinter dem
		// Komma eine Leerzeile, die nicht zum Namen des Producers geh�rt
		// Folgender Code entfernt diese Leerzeile
		for (int i = 0; i < input.length; i++) {
			char[] c = input[i].toCharArray();
			if (c[0] == ' ') {
				char[] c2 = new char[c.length - 1];
				java.lang.System.arraycopy(c, 1, c2, 0, c.length - 1);
				input[i] = new String(c2);
			}
		}

		// �berprufen, dass richtig eingegeben wurde
		// wurde nicht, dann wird nur ausgegeben, dass nicht gelesen wurden
		// konnte
		int number = 0;
		for (int i = 0; i < input.length; i++) {
			boolean contained = false;
			for (int k = 0; k < producers.length; k++) {
				if (input[i].equals(producers[k])) {
					number++;
					contained = true;
					break;
				}
			}
			if (!contained) {
				System.out.println("Mit der Eingabe " + input[i] + "ist nichts anzufangen");
				input[i] = null;
			}
		}
		String[] enterOnProducers = new String[number];
		int k = 0;
		for (int i = 0; i < input.length; i++) {
			if (input[i] != null) {
				enterOnProducers[k] = input[i];
				k++;
			}
		}

		PayloadRegisterOnProducer payload = new PayloadRegisterOnProducer(this.consumerID, enterOnProducers);
		Message m = new Message(MessageType.RegisterOnProducer, payload);

		Message answer = sendandGetMessage(m, server);
		if (answer.getType() == MessageType.RegisterOnProducer) {
			PayloadRegisterOnProducer answerPayload = (PayloadRegisterOnProducer) answer.getPayload();
			String[] answerProducers = answerPayload.getProducers();
			if (answerProducers != null) {
				System.out.print("Der Einschreibevorgang war f�r die/den folgenden Producer nicht erfolgreich: ");
				for (int i = 0; i < answerProducers.length; i++) {
					System.out.print(answerProducers[i]);
				}
				System.out.print("\n");
			} else {
				System.out.println(
						"Der Einschreibevorgang war erfolgreich! Sie werden bei Push-Nachrichten Ihrer abonnierten Produzenten informiert...");

			}
		} else {
			throw new RuntimeException("payload der message stimmt nicht");
		}
		closeSocket(server);
	}

	public boolean unsubscribeFromProducer(String[] name) {
	}

	public String[] getProducers() {

		PayloadProducer payload = new PayloadProducer(null);
		Message m = new Message(MessageType.getProducer, payload);

		Message answer = sendandGetMessage(m, server);
		if (answer.getType() == MessageType.getProducer) {
			PayloadProducer answerPayload = (PayloadProducer) answer.getPayload();
			return answerPayload.getProducers();
		} else {
			throw new RuntimeException("payload der message stimmt nicht");
		}
	}

	/**
	 * 
	 * @return the ID privided by the server.
	 */
	public String registerOnServer() {
		// ich hab jetzt eine Methode erstellt, die ein Socket zur�ckliefert,
		// welches mit dem Server kommuniziert. Das alles in ner eigenen
		// Methode, da f�r das
		// Einschreiben auf Produzenten, ne eigener Socket ben�tigt wird
		// (sp�ter, wenn der Konsument registriert wurde und der Anwender sich
		// auf neuen
		// einschreiben will, ist zum Beispiel ein Socket zur Registrierung
		// nicht mehr da) (oder sollte man eher ein SOcket im Konstruktor
		// erschaffen und erst
		// beim Beenden des Consumers schlei�en ?????????????????????????????)
		Socket server = getTCPConnectionToServer();
		PayloadRegisterOnServer payload = new PayloadRegisterOnServer(0, null);
		Message m = new Message(MessageType.RegisterOnServer, payload);
		// antwort verarbeiten
		Message answer = sendandGetMessage(m, server);

		if (answer.getType() == MessageType.RegisterOnServer) {
			PayloadRegisterOnServer answerPayload = (PayloadRegisterOnServer) answer.getPayload();
			consumerID = answerPayload.getId();
			multicastAddress = answerPayload.getMulticastAddress();
		} else {
			throw new RuntimeException("payload der message stimmt nicht");
		}
		closeSocket(server);

	}

	public boolean deregisterFromServer() {

		PayloadDeregister payload = new PayloadDeregister(consumerID);
		Message m = new Message(MessageType.Deregister, payload);

		Socket server = getTCPConnectionToServer();
		Message answer = sendandGetMessage(m, server);
		if (answer.getType() == MessageType.Deregister) {
			PayloadDeregister answerPayload = (PayloadDeregister) answer.getPayload();
			if (answerPayload.getConsignorID() != 0) {
				System.out.println("Fehler: Irgendetwas ist beim Abmelden falsch gelaufen");
			}
		} else {
			throw new RuntimeException("payload der message stimmt nicht");
		}

		closeSocket(server);
	}

	//
	public boolean registerOnMulticastGroup() {
		MulticastSocket udpSocket = null;
		try {
			udpSocket = new MulticastSocket();

			udpSocket.joinGroup(multicastAddress);
		} catch (IOException e) {
			System.out.println("IOFehler beim Registrieren in der Multicastgruppe");
			e.printStackTrace();

		}
		// !!!!!!!!!!!!!!!!!!!!!!!!!
		// Wird nicht geclost
		// aber ich wei�auch nicht wie und wo

		Thread t = new Thread(new GetMessage(udpSocket));
		t.start();

	}

	/**
	 * 
	 * @param message
	 *            The message to be filled with content
	 * @param address
	 *            The address of the server
	 * @return The filled message. If the operation was not successful null.
	 * @throws ClassNotFoundException 
	 */
	private Message sendAndGetMessage(Message message, InetAddress address) {
		Socket server;
		try {
			server = new Socket(address, serverPort);

			Message answer = null;
			ObjectOutputStream out = null;
			ObjectInputStream in = null;

			out = new ObjectOutputStream(server.getOutputStream());
			in = new ObjectInputStream(server.getInputStream());
			out.writeObject(message);
			answer = (Message) in.readObject();

			in.close();
			out.close();
			server.close();
			return answer;
		} catch (Exception e) {
			return null;
		}
	}

	class GetMessage implements Runnable {
		MulticastSocket udps;
		// hier auch nochmal:
		// Das mit den pipes funktioniert leider so nicht, wie ichmir das
		// vorgestellt habe ... deswegen die unsch�ne variante

		public GetMessage(MulticastSocket udps) {
			this.udps = udps;
		}

		@Override
		public void run() {
			DatagramPacket dp = null;// `??????
			try {
				udps.receive(dp);
				Message rsp = Message.getMessageFromDatagramPacket(dp);
				// schreibt noch nen ; dahinter, damit ich oben sehen kann, ob
				// mehrere Messages kamen
				// pw.write(m.getPayload() + ";");
				System.out.println("Sie haben eine neue Push-Mitteilung:");
				// er schreibt ja jetzt einfach raus ...
				// vlt funktioniert dies nicht, weil im hauptthread er gerade
				// auf ne Eingabe wartet ... vlt muss man dann hier den
				// hauptthread einschl�fern und
				// nach der Ausgabe wieder aufwecken?!
				if (rsp.getType() == MessageType.Message) {
					PayloadMessage answerPayload = (PayloadMessage) rsp.getPayload();

					System.out.println(answerPayload.getConsignorName() + " meldet: \n" + answerPayload.getText());
				} else {
					throw new RuntimeException("Falscher Payload in GetMessage");
				}
			} catch (IOException e) {
				System.out.println("Fehler beim bearbeiten der erhaltenen UDP-Message");
				e.printStackTrace();
			}
		}
	}
}
