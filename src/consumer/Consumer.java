package consumer;

import java.io.*;
import java.net.*;

import message.*;

public class Consumer {
	private static int serverPort = 55555;
	private int consumerID;
	private InetAddress serverAddress;
	// private InetAddress multicastAddress;

	/**
	 * Used to interact with a MessageServer
	 * 
	 * @param address
	 *            address of the server
	 * @throws IOException
	 *             when the address is not reachable.
	 */
	public Consumer(String address) throws IOException {
		this.serverAddress = InetAddress.getByName(address);
		if (!testConnection(serverAddress, 1000))
			throw new IOException("There ist no server on the specified address");
	}

	/**
	 * Checks if it is possible to establish a TCP connection using the
	 * "serverPort"
	 * 
	 * @param adress
	 *            The address of the server to be checked
	 * @param timeout
	 *            Timeout of the connection
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
		return ((PayloadGetSubscriptions) response.getPayload()).getSubscriptions(); // Always expects a string array, even if there are no producers available (then its just empty)
	}

	/**
	 * Subscribes this user to the given producers
	 * 
	 * @param producers
	 *            The names of the producers (can not be null)
	 * @return an array of producers the consumer is now newly subscribed to
	 */
	public String[] subscribeToProducers(String[] producers) {
		if (producers == null)
			throw new IllegalArgumentException("'producers' may not be null");
		Message answer = this.sendAndGetMessage(
				new Message(MessageType.SubscribeProducers, new PayloadSubscribeProducers(producers)), serverAddress);
		return ((PayloadSubscribeProducers) answer.getPayload()).getToBeSubscribed();
	}

	/**
	 * Unsubscribes this user from the given producers
	 * 
	 * @param producers
	 *            The names of the producers (can not be null)
	 * @return an array of producers the consumer is now unsubscribed from
	 */
	public String[] unsubscribeFromProducers(String[] producers) {
		if (producers == null)
			throw new IllegalArgumentException("'producers' may not be null");
		Message answer = this.sendAndGetMessage(
				new Message(MessageType.UnsubscribeProducers, new PayloadUnsubscribeProducers(producers)),
				serverAddress);
		return ((PayloadUnsubscribeProducers) answer.getPayload()).getToBeUnsubscribed();
	}

	/**
	 * Fetches all available producers
	 * 
	 * @return all available producers
	 */
	public String[] getProducers() {
		Message answer = this.sendAndGetMessage(new Message(MessageType.getProducerList, null), serverAddress);
		return ((PayloadGetProducerList) answer.getPayload()).getProducers();
	}

	/**
	 * Registers the user on the server
	 * 
	 * @return
	 */
	public boolean registerOnServer() {
		Message answer = sendAndGetMessage(new Message(MessageType.RegisterConsumer, null), serverAddress);

		PayloadRegisterConsumer answerPayload = (PayloadRegisterConsumer) answer.getPayload();
		this.consumerID = answerPayload.getId();
		// this.multicastAddress = answerPayload.getMulticastAddress();
		return true; // TODO Operation successful?
	}

	/**
	 * Registers the user on the server
	 * 
	 * @return if the operation was successful
	 */
	public boolean deregisterFromServer() {

		Message answer = sendAndGetMessage(
				new Message(MessageType.DeregisterConsumer, new PayloadDeregisterConsumer(consumerID)), serverAddress);
		PayloadDeregisterConsumer answerPayload = (PayloadDeregisterConsumer) answer.getPayload();

		return answerPayload.getConsignorID() != 0;

	}

	/**
	 * Sends a message object to the server via TCP and receives it afterwards
	 * 
	 * @param message
	 *            the message to be sent
	 * @param address
	 *            the address the message is to be sent to
	 * @return the answer from the server
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

	/*
	 * public boolean registerOnMulticastGroup() { MulticastSocket udpSocket =
	 * null; try { udpSocket = new MulticastSocket();
	 * 
	 * udpSocket.joinGroup(multicastAddress); } catch (IOException e) {
	 * System.out.println("IOFehler beim Registrieren in der Multicastgruppe");
	 * e.printStackTrace();
	 * 
	 * }
	 * 
	 * Thread t = new Thread(new GetMessage(udpSocket)); t.start();
	 * 
	 * }
	 */
	/**
	 * 
	 * @param message
	 *            The message to be filled with content
	 * @param address
	 *            The address of the server
	 * @return The filled message. If the operation was not successful null.
	 * @throws ClassNotFoundException
	 */

	/*
	 * class GetMessage implements Runnable { MulticastSocket udps;
	 * 
	 * public GetMessage(MulticastSocket udps) { this.udps = udps; }
	 * 
	 * @Override public void run() { DatagramPacket dp = null;// `?????? try {
	 * udps.receive(dp); Message rsp = Message.getMessageFromDatagramPacket(dp);
	 * // schreibt noch nen ; dahinter, damit ich oben sehen kann, ob // mehrere
	 * Messages kamen // pw.write(m.getPayload() + ";");
	 * System.out.println("Sie haben eine neue Push-Mitteilung:"); // er
	 * schreibt ja jetzt einfach raus ... // vlt funktioniert dies nicht, weil
	 * im hauptthread er gerade // auf ne Eingabe wartet ... vlt muss man dann
	 * hier den // hauptthread einschlï¿½fern und // nach der Ausgabe wieder
	 * aufwecken?! if (rsp.getType() == MessageType.Message) { PayloadMessage
	 * answerPayload = (PayloadMessage) rsp.getPayload();
	 * 
	 * System.out.println(answerPayload.getConsignorName() + " meldet: \n" +
	 * answerPayload.getText()); } else { throw new
	 * RuntimeException("Falscher Payload in GetMessage"); } } catch
	 * (IOException e) {
	 * System.out.println("Fehler beim bearbeiten der erhaltenen UDP-Message");
	 * e.printStackTrace(); } } }
	 */
}
