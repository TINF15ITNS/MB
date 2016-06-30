package Consumer;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.IOException;
import java.io.PipedReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import MessageServer.Message;
import MessageServer.MessageType;

public class Consumer implements ConsumerIF {
	private String name;
	private int consumerID;
	private Set<String> producers;
	private InetAddress ssadr;
	private int portServer;
	private InetAddress multicastAddress;
	private PipedReader pr;
	private Scanner scanner;

	public static void main(String[] args) {

	}

	public Consumer(String name, InetAddress ssadr, int portServer) {
		this.ssadr = ssadr;
		this.portServer = portServer;
		scanner = new Scanner(System.in);
		producers = new HashSet<>();

	}

	// verdammt das mit den pipes funktioniert so nciht, wie ich mir das vorgestellt habe
	/*
	 * private void getNewMessages() { BufferedReader br = new BufferedReader(pr); String[] newMessages = null; try { if (br.ready()) { newMessages =
	 * br.readLine().split(";"); } System.out.println(Sie haben neue Nachrichten); for (int i = 0; i < newMessages.length; i++) {
	 * 
	 * } } catch (IOException e) { System.out.println("IOFehler beim lesen aus der Pipe"); e.printStackTrace(); }
	 * 
	 * 
	 * }
	 */

	@Override
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
				exit = false;
				deregister();
				break;
			default:
			}
		}
	}

	@Override
	public void registerOnProducers() {
		Socket server = getTCPConnectionToServer();
		getOfferofProducers(server);
		System.out.println("Sie können sich für die folgenden Produzenten einschreiben: \n" + producers.toString());

		System.out.print("\nGeben Sie den Namen, der betreffenden Produzenten, für die Sie sich einschreiben wollen, mit einem \",\" getrennt ein: ");
		String[] input = scanner.nextLine().split(",");

		// Durch die Eingabe des User entsteht bei Mehrfachauswahl hinter dem Komma eine Leerzeile, die nicht zum Namen des Producers gehört
		// Folgender Code entfernt diese Leerzeile
		for (int i = 0; i < input.length; i++) {
			char[] c = input[i].toCharArray();
			if (c[0] == ' ') {
				char[] c2 = new char[c.length - 1];
				java.lang.System.arraycopy(c, 1, c2, 0, c.length - 1);
				input[i] = new String(c2);
			}
		}
		String payload = "";

		// überprufen, dass richtig eingegeben wurde
		for (int i = 0; i < input.length; i++) {
			// i = 1
			if (!producers.contains(input[i])) {
				System.out.println("Der Produzent " + input[i] + " existiert nicht ... ");

				String[] input2 = new String[input.length - 1];
				java.lang.System.arraycopy(input, 0, input2, 0, i);
				java.lang.System.arraycopy(input, i + 1, input2, i, input.length - i - 1);
				input = input2;
			}
		}

		for (int i = 0; i < input.length; i++) {
			payload += input[i] + ";";
		}

		Message m = new Message(MessageType.RegisterOnProducer, this.consumerID, payload);
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

	private void getOfferofProducers(Socket server) {
		// bei Client anfragen nach producer, ist payload null
		// Antwort Message ist im Payload die ganzen ProducerNamen mit ; getrennt
		// hab ich jetzt mal so definiert
		Message m = new Message(MessageType.getProducer, this.consumerID, null);
		Message answer = sendandGetMessage(m, server);
		String[] help = answer.getPayload().split(";");
		for (int i = 0; i < help.length; i++) {
			producers.add(help[i]);
		}
	}

	@Override
	public void registerOnServer() {
		// ich hab jetzt eine Methode erstellt, die ein Socket zurückliefert, welches mit dem Server kommuniziert. Das alles in ner eigenen Methode, da für das
		// Einschreiben auf Produzenten, ne eigener Socket benötigt wird (später, wenn der Konsument registriert wurde und der Anwender sich auf neuen
		// einschreiben will, ist zum Beispiel ein Socket zur Registrierung nicht mehr da) (oder sollte man eher ein SOcket im Konstruktor erschaffen und erst
		// beim Beenden des Consumers schleißen ?????????????????????????????)
		Socket server = getTCPConnectionToServer();
		// das zu verschicken Message-Objekt wird angelegt
		Message m = new Message(MessageType.RegisterOnServer, 0, this.name);
		// antwort verarbeiten
		Message answer = sendandGetMessage(m, server);
		String[] answerPayload = answer.getPayload().split(";");
		consumerID = (int) new Integer(answerPayload[0]);
		try {
			multicastAddress = InetAddress.getByName(answerPayload[1]);
		} catch (UnknownHostException e1) {
			System.out.println("Die zurückgeleiferte Multicastadresse stimmt nicht");
			e1.printStackTrace();
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

	@Override
	public void deregister() {
		Message m = new Message(MessageType.Deregister, this.consumerID, null);

		InetAddress iadrServer = null;
		try {
			iadrServer = InetAddress.getByName("localhost");
		} catch (UnknownHostException e) {
			// diese Fall wird hier nicht eintreten, da localhost wohl kaum unbekannt sein kann ...
		}
		DatagramPacket dp = Message.getMessageAsDatagrammPacket(m, iadrServer, portServer);

		DatagramSocket udpSocket;
		try {
			// hier könnte man den UDP-Socket aufn nen lokalen port binden
			udpSocket = new DatagramSocket();
			// empfängt nun nur Nachrichten vom Server nciht von anderen möglicherweise vorhandenen UDP-Sockets
			udpSocket.connect(iadrServer, portServer);
			udpSocket.setSoTimeout(5000);

			udpSocket.send(dp);
		} catch (SocketException e) {
			System.out.println("Der UDP-Socket konnte nicht erzeugt werden ...");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IO-Fehler beim Senden des Datagram-Packets");
			e.printStackTrace();
		}
	}

	@Override
	public void registerOnMulticastGroup() {
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
		// aber ich weißauch nicht wie und wo

		pr = new PipedReader();

		// Thread t = new Thread(new GetMessage(udpSocket, pr));
		Thread t = new Thread(new GetMessage(udpSocket));
		t.start();

	}

	private Socket getTCPConnectionToServer() {
		Socket s = null;
		try {
			s = new Socket(ssadr, portServer);
		} catch (UnknownHostException e) {
			System.out.println("Die Serveradresse stimmt nicht");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IO-Fehler bei Socketerstellung zum Server");
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

			dec = new XMLDecoder(server.getInputStream());
			answer = (Message) dec.readObject();

		} catch (IOException e) {
			System.out.println("Fehler beim En- un Decodieren");
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

	class GetMessage implements Runnable {
		MulticastSocket udps;
		// hier auch nochmal:
		// Das mit den pipes funktioniert leider so nicht, wie ichmir das vorgestellt habe ... deswegen die unschöne variante
		// PipedWriter pw;

		// public GetMessage(MulticastSocket udps, PipedReader pr){
		public GetMessage(MulticastSocket udps) {
			this.udps = udps;
			/*
			 * pw = new PipedWriter(); try { pw.connect(pr); } catch (IOException e) { System.out.println("IOFehler beim verbinden der beiden Pipes"); }
			 */
		}

		@Override
		public void run() {
			DatagramPacket dp = null;// `??????
			try {
				udps.receive(dp);
				Message m = Message.getMessageFromDatagramPacket(dp);
				// schreibt noch nen ; dahinter, damit ich oben sehen kann, ob mehrere Messages kamen
				// pw.write(m.getPayload() + ";");
				System.out.println("Sie haben eine neue Push-Mitteilung:");
				// !!!!!!!!!!!!!!!!!!!!!!!!
				// in der Message Message soll im payload als erster teil der Name des Absenders stehen und danach die Nachricht
				// !!!!!!!!!!!!!!!!!!!!!!!!!
				String[] newMessage = m.getPayload().split(";");
				System.out.println(newMessage[0] + " meldet: \n" + newMessage[1]);
			} catch (IOException e) {
				System.out.println("Fehler beim bearbeiten der erhaltenen UDP-Message");
				e.printStackTrace();
			}
		}
	}
}
