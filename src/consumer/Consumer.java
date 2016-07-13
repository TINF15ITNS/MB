package consumer;

import java.io.*;
import java.net.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import message.*;

public class Consumer implements ConsumerIF {
	private static int serverPort = 55555;
	private int consumerID;
	private InetAddress mcastadr;
	private InetAddress serverAddress;
	private HashSet<String> subscriptions;
	private HashSet<String> producerList;
	MulticastSocket udpSocket = null;

	/**
	 * Used to interact with a MessageServer
	 * 
	 * @param address
	 *            address of the server
	 * @throws IOException
	 *             when the address is not reachable.
	 */
	public Consumer(String address) throws IOException {
		subscriptions = new HashSet<>();
		producerList = new HashSet<>();
		this.serverAddress = InetAddress.getByName(address);
		if (!Util.testConnection(serverAddress, serverPort, 1000))
			throw new IOException("There ist no server on the specified address");

	}

	/**
	 * Registers the user on the server
	 */
	@Override
	public void registerOnServer() {

		Message answer = Util.sendAndGetMessage(new Message(MessageType.RegisterConsumer, null), serverAddress, serverPort);

		PayloadRegisterConsumer answerPayload = (PayloadRegisterConsumer) answer.getPayload();
		this.consumerID = answerPayload.getId();
		this.mcastadr = answerPayload.getMulticastAddress();
		// TODO Operation successful?

		try {
			udpSocket = new MulticastSocket();
			joinMulticastGroup();
		} catch (IOException e) {
			System.out.println("IOFehler beim Erstellen des MulticastSockets oder beim Einschreiben in die Multicast-Gruppe");
			e.printStackTrace();
		}

		Thread t = new Thread(new WaitForMessage(udpSocket));
		t.start();
	}

	/**
	 * Fetches all available producers
	 * 
	 * @return all available producers
	 */
	@Override
	public String[] getProducers() {

		Message answer = Util.sendAndGetMessage(MessageFactory.createRequestProducerListMsg(), serverAddress, serverPort);
		for (String s : ((PayloadGetProducerList) answer.getPayload()).getProducers()) {
			producerList.add(s);
		}
		return producerList.toArray(new String[0]);
	}

	/**
	 * Subscribes this user to the given producers
	 * 
	 * @param producers
	 *            The names of the producers (can not be null)
	 * @return a list of producers, it wasn't possible to subscribe on
	 */
	@Override
	public String[] subscribeToProducers(String[] producers) {
		if (producers == null)
			throw new IllegalArgumentException("'producers' may not be null");

		List<String> list = new LinkedList<>();
		for (String s : producers) {
			// existiert dieser Producer?
			if (this.producerList.contains(s)) {
				subscriptions.add(s);
			} else {
				list.add(s);
			}
		}
		return list.toArray(new String[0]);
	}

	/**
	 * Fetches a list of all producers this consumer is subscribed to
	 * 
	 * @return a list of producers this consumer is subscribed to
	 */
	@Override
	public String[] getSubscriptions() {
		return subscriptions.toArray(new String[0]);
		/*
		 * Deine Lösung Fabian, aber mit meiner brauchen wir das hier nicht mehr
		 * 
		 * Message response = Util.sendAndGetMessage(new Message(MessageType.getSubscriptions, null), serverAddress, serverPort); return
		 * ((PayloadGetSubscriptions) response.getPayload()).getSubscriptions(); // Always expects a string array, even if there are no producers available
		 * (then its just empty)
		 */
	}

	/**
	 * Unsubscribes this user from the given producers
	 * 
	 * @param producers
	 *            The names of the producers (can not be null)
	 * @return
	 */
	@Override
	public String[] unsubscribeFromProducers(String[] producers) {
		if (producers == null)
			throw new IllegalArgumentException("'producers' may not be null");

		List<String> list = new LinkedList<>();
		for (String s : producers) {
			if (!subscriptions.remove(s)) {
				list.add(s);
			}
		}
		return list.toArray(new String[0]);
		/*
		 * Message answer = Util.sendAndGetMessage( new Message(MessageType.UnsubscribeProducers, new PayloadUnsubscribeProducers(producers)), serverAddress,
		 * serverPort); return ((PayloadUnsubscribeProducers) answer.getPayload()).getToBeUnsubscribed();
		 */

	}

	/**
	 * Deregisters the user on the server
	 * 
	 * @return if the operation was successful
	 */
	@Override
	public boolean deregisterFromServer() {
		Message answer = Util.sendAndGetMessage(new Message(MessageType.DeregisterConsumer, new PayloadDeregisterConsumer(consumerID)), serverAddress,
				serverPort);

		PayloadDeregisterConsumer answerPayload = (PayloadDeregisterConsumer) answer.getPayload();

		try {
			leaveMulticastGroup();
		} catch (IOException e) {
			System.out.println("IOFehler beim Verlassen der Mutlicast-Gruppe");
			e.printStackTrace();
		}

		return answerPayload.getSenderID() != 0;
	}

	private void joinMulticastGroup() throws IOException {
		udpSocket.joinGroup(mcastadr);
	}

	private void leaveMulticastGroup() throws IOException {
		udpSocket.leaveGroup(mcastadr);
	}

	/**
	 * 
	 * The class is listening for messages from the server and prints them on the console
	 *
	 */
	private class WaitForMessage implements Runnable {
		MulticastSocket udps;

		public WaitForMessage(MulticastSocket udps) {
			this.udps = udps;
		}

		@Override
		public void run() {
			// TODO Idee mit Pipes zu arbeiten und dem User ne Möglichkeit zu geben, abzufragen, ob es neue Nachrichten gibt...
			DatagramPacket dp = null;
			while (true) {
				try {
					udps.receive(dp);
					Message m = Message.getMessageFromDatagramPacket(dp);

					switch (m.getType()) {

					case DeregisterProducer:
						if (m.getType() != MessageType.DeregisterProducer)
							throw new RuntimeException("Falscher Payload in GetMessage");
						PayloadProducer pp = (PayloadProducer) m.getPayload();
						System.out.println("Der Producer " + pp.getName()
								+ " hat den Dienst eingestellt. Sie können leider keine Push-Nachrichten mehr von ihm erhalten...");
						producerList.remove(pp.getName());
						subscriptions.remove(pp.getName());
						break;

					case Message:
						if (m.getType() != MessageType.Message)
							throw new RuntimeException("Falscher Payload in GetMessage");
						System.out.println("Sie haben eine neue Push-Mitteilung:");
						// er schreibt ja jetzt einfach raus ... vlt funktioniert dies nicht, weil im hauptthread er gerade auf ne Eingabe wartet ... vlt muss
						// man dann hier den Hauptthread einschläfern und nach der Ausgabe wieder aufwecken?!
						PayloadMessage payload = (PayloadMessage) m.getPayload();
						if (subscriptions.contains(payload.getSender()))
							System.out.println(payload.getSender() + " meldet: \n" + payload.getMessage());
						break;

					default:
						break;

					}

				} catch (IOException e) {
					System.out.println("Fehler beim Bearbeiten der erhaltenen UDP-Message");
					e.printStackTrace();
				}
			}
		}
	}

}
