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
	 * @param timeout
	 *            Timout of the connection
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

		Message response = this.sendAndGetMessage(new Message(MessageType.getSubscriptions, null), serverAddress);
		return ((PayloadGetSubscriptions) response.getPayload()).getSubscriptions();

	}

	/**
	 * 
	 * @param name
	 *            The name of the producer (can not contain whitespaces)
	 * @return
	 */
	public boolean subscribeToProducers(String[] producers) {
		this.sendAndGetMessage(new Message(MessageType.SubscribeProducers, new PayloadSubscribeProducers(producers)),
				serverAddress);
		return true;
	}

	public boolean unsubscribeFromProducers(String[] producers) {
		this.sendAndGetMessage(
				new Message(MessageType.UnsubscribeProducers, new PayloadUnsubscribeProducers(producers)),
				serverAddress);
		return true;
	}

	public String[] getProducers() {
		Message answer = this.sendAndGetMessage(new Message(MessageType.getProducerList, null), serverAddress);
		return ((PayloadGetProducerList) answer.getPayload()).getProducers();
	}

	/**
	 * 
	 * @return the ID provided by the server.
	 */
	public boolean registerOnServer() {
		Message answer = sendAndGetMessage(new Message(MessageType.RegisterConsumer, null), serverAddress);

		PayloadRegisterConsumer answerPayload = (PayloadRegisterConsumer) answer.getPayload();
		this.consumerID = answerPayload.getId();
		this.multicastAddress = answerPayload.getMulticastAddress();
		return true;
	}

	public boolean deregisterFromServer() {

		PayloadDeregisterConsumer payload = new PayloadDeregisterConsumer(consumerID);
		Message m = new Message(MessageType.Deregister, payload);

		Socket server = getTCPConnectionToServer();
		Message answer = sendandGetMessage(m, server);
		if (answer.getType() == MessageType.Deregister) {
			PayloadDeregisterConsumer answerPayload = (PayloadDeregisterConsumer) answer.getPayload();
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
