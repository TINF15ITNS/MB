package Consumer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import Message.*;

public class Consumer implements ConsumerIF {
	private String name;
	private int consumerID;
	private InetAddress ssadr;
	private int portServer;
	private InetAddress multicastAddress;
	private Scanner scanner;

	public static void main(String[] args) {

	}

	public Consumer(String name, InetAddress ssadr, int portServer) {
		this.ssadr = ssadr;
		this.portServer = portServer;
		scanner = new Scanner(System.in);

	}

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
		String[] producers = getOfferofProducers(server);
		System.out.println("Sie können sich für die folgenden Produzenten einschreiben:");
		for (int i = 0; i < producers.length; i++) {
			System.out.print(producers[i] + " , ");
		}

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

		// überprufen, dass richtig eingegeben wurde
		// wurde nicht, dann wird nur ausgegeben, dass nicht gelesen wurden konnte
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

		PayloadForMessageTypeRegisterOnProducer payload = new PayloadForMessageTypeRegisterOnProducer(this.consumerID, enterOnProducers);
		Message m = new Message(MessageType.RegisterOnProducer, payload);

		Message answer = sendandGetMessage(m, server);
		if (answer.getType() == MessageType.RegisterOnProducer) {
			PayloadForMessageTypeRegisterOnProducer answerPayload = (PayloadForMessageTypeRegisterOnProducer) answer.getPayload();
			String[] answerProducers = answerPayload.getProducers();
			if (answerProducers != null) {
				System.out.print("Der Einschreibevorgang war für die/den folgenden Producer nicht erfolgreich: ");
				for (int i = 0; i < answerProducers.length; i++) {
					System.out.print(answerProducers[i]);
				}
				System.out.print("\n");
			} else {
				System.out.println("Der Einschreibevorgang war erfolgreich! Sie werden bei Push-Nachrichten Ihrer abonnierten Produzenten informiert...");

			}
		} else {
			throw new RuntimeException("payload der message stimmt nicht");
		}
		closeSocket(server);
	}

	private String[] getOfferofProducers(Socket server) {

		PayloadForMessageTypegetProducer payload = new PayloadForMessageTypegetProducer(null);
		Message m = new Message(MessageType.getProducer, payload);

		Message answer = sendandGetMessage(m, server);
		if (answer.getType() == MessageType.getProducer) {
			PayloadForMessageTypegetProducer answerPayload = (PayloadForMessageTypegetProducer) answer.getPayload();
			return answerPayload.getProducers();
		} else {
			throw new RuntimeException("payload der message stimmt nicht");
		}
	}

	@Override
	public void registerOnServer() {
		// ich hab jetzt eine Methode erstellt, die ein Socket zurückliefert, welches mit dem Server kommuniziert. Das alles in ner eigenen Methode, da für das
		// Einschreiben auf Produzenten, ne eigener Socket benötigt wird (später, wenn der Konsument registriert wurde und der Anwender sich auf neuen
		// einschreiben will, ist zum Beispiel ein Socket zur Registrierung nicht mehr da) (oder sollte man eher ein SOcket im Konstruktor erschaffen und erst
		// beim Beenden des Consumers schleißen ?????????????????????????????)
		Socket server = getTCPConnectionToServer();
		PayloadForMessageTypeRegisterOnServer payload = new PayloadForMessageTypeRegisterOnServer(0, null);
		Message m = new Message(MessageType.RegisterOnServer, payload);
		// antwort verarbeiten
		Message answer = sendandGetMessage(m, server);

		if (answer.getType() == MessageType.RegisterOnServer) {
			PayloadForMessageTypeRegisterOnServer answerPayload = (PayloadForMessageTypeRegisterOnServer) answer.getPayload();
			consumerID = answerPayload.getId();
			multicastAddress = answerPayload.getMulticastAddress();
		} else {
			throw new RuntimeException("payload der message stimmt nicht");
		}
		closeSocket(server);

	}

	@Override
	public void deregister() {
		PayloadForMessageTypeDeregister payload = new PayloadForMessageTypeDeregister(consumerID);
		Message m = new Message(MessageType.Deregister, payload);

		Socket server = getTCPConnectionToServer();
		Message answer = sendandGetMessage(m, server);
		if (answer.getType() == MessageType.Deregister) {
			PayloadForMessageTypeDeregister answerPayload = (PayloadForMessageTypeDeregister) answer.getPayload();
			if (answerPayload.getConsignorID() != 0) {
				System.out.println("Fehler: Irgendetwas ist beim Abmelden falsch gelaufen");
			}
		} else {
			throw new RuntimeException("payload der message stimmt nicht");
		}

		closeSocket(server);
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

	private void closeSocket(Socket s) {
		if (s != null) {
			try {
				s.close();
			} catch (IOException e) {
				System.out.println("Socket lässt sich nicht schließen");
				e.printStackTrace();
			}
		}
	}

	private Message sendandGetMessage(Message m, Socket server) {
		Message answer = null;
		ObjectOutputStream out = null;
		ObjectInputStream in = null;
		try {
			out = new ObjectOutputStream(server.getOutputStream());
			out.writeObject(m);
			in = new ObjectInputStream(server.getInputStream());
			answer = (Message) in.readObject();
		} catch (ClassNotFoundException e) {
			System.out.println("Fehler beim Lesen des Objektes");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IOFehler beim Senden und Empfangen der Messages");
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
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

		public GetMessage(MulticastSocket udps) {
			this.udps = udps;
		}

		@Override
		public void run() {
			DatagramPacket dp = null;// `??????
			try {
				udps.receive(dp);
				Message rsp = Message.getMessageFromDatagramPacket(dp);
				// schreibt noch nen ; dahinter, damit ich oben sehen kann, ob mehrere Messages kamen
				// pw.write(m.getPayload() + ";");
				System.out.println("Sie haben eine neue Push-Mitteilung:");
				// er schreibt ja jetzt einfach raus ...
				// vlt funktioniert dies nicht, weil im hauptthread er gerade auf ne Eingabe wartet ... vlt muss man dann hier den hauptthread einschläfern und
				// nach der Ausgabe wieder aufwecken?!
				if (rsp.getType() == MessageType.Message) {
					PayloadForMessageTypeMessage answerPayload = (PayloadForMessageTypeMessage) rsp.getPayload();

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
