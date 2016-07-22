/**
 * 
 */
package consumer;

import java.io.*;
import java.net.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import message.*;

/**
 * 
 * @author Nikolai Seip, Sebastian Mattheis, Fabian Hinz
 *
 */
public class Consumer implements ConsumerIF {
	private static int serverPort = 55555;
	private int consumerID;
	private boolean registered = false;
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

	@Override
	public boolean registerOnServer() {
		Message answer;

		try {
			answer = Util.sendAndGetMessage(new Message(MessageType.RegisterConsumer, null), serverAddress, serverPort);
		} catch (IOException e) {
			registered = false;
			return false; //If there is no connection to the server, the consumer cannot be registered.
		}

		PayloadRegisterConsumer answerPayload = (PayloadRegisterConsumer) answer.getPayload();
		if (!answerPayload.getSuccess()) {
			registered = false;
			return false; //If the process was unsuccessful the consumer cannot be registered.
		}
		this.consumerID = answerPayload.getId();
		this.mcastadr = answerPayload.getMulticastAddress();
		try

		{
			udpSocket = new MulticastSocket();
			udpSocket.joinGroup(mcastadr);
		} catch (IOException e) {
			registered = false;
			return false;
		}

		Thread t = new Thread(new WaitForMessage(udpSocket));
		t.start();
		registered = answerPayload.getSuccess();
		return true;
	}

	@Override
	public String[] getProducers() {
		Message answer;
		try {
			answer = Util.sendAndGetMessage(MessageFactory.createRequestProducerListMsg(), serverAddress, serverPort);
		} catch (IOException e) {
			return null;
		}
		PayloadGetProducerList answerPayload = (PayloadGetProducerList) answer.getPayload();
		if (!answerPayload.getSuccess())
			return null;
		return (answerPayload.getProducers());
	}

	@Override
	public String[] subscribeToProducers(String[] producers) {
		if (producers == null)
			return new String[0]; //There are no producers to be subscribed to, so there are none where it was not possible
		
		//TODO passt das so?
		List<String> unsuccessfulProducers = new LinkedList<>();
		for (String s : producers) {
			// existiert dieser Producer?
			if (this.producerList.contains(s)) {
				subscriptions.add(s);
			} else {
				unsuccessfulProducers.add(s);
			}
		}
		return unsuccessfulProducers.toArray(new String[0]);
	}

	@Override
	public String[] getSubscriptions() {
		return subscriptions.toArray(new String[subscriptions.size()]);
	}

	@Override
	public String[] unsubscribeFromProducers(String[] producers) {
		if (producers == null) return new String[0]; //There are no producers to be unsubscribed from, so there are none where it was not possible

		List<String> list = new LinkedList<>();
		for (String s : producers) {
			if (!subscriptions.remove(s)) {
				list.add(s);
			}
		}
		return list.toArray(new String[list.size()]);
	}

	@Override
	public boolean deregisterFromServer() {
		Message answer;
		try {
			answer = Util.sendAndGetMessage(new Message(MessageType.DeregisterConsumer, new PayloadDeregisterConsumer(consumerID)), serverAddress, serverPort);
		} catch (IOException e) {
			return false;
		}

		PayloadDeregisterConsumer answerPayload = (PayloadDeregisterConsumer) answer.getPayload();
		if(!answerPayload.getSuccess()) return false;

		try {
			udpSocket.leaveGroup(mcastadr);
		} catch (IOException e) {
			//TODO return true/false?
		}
		registered = !answerPayload.getSuccess();
		return answerPayload.getSuccess();
	}

	public boolean isRegistered() {
		return registered;
	}

	/**
	 * 
	 * The class is listening for messages from the server and prints them on
	 * the console
	 *
	 */
	private class WaitForMessage implements Runnable {
		private MulticastSocket udps;

		public WaitForMessage(MulticastSocket udps) {
			this.udps = udps;
		}

		@Override
		public void run() {
			// TODO Idee mit Pipes zu arbeiten und dem User ne Möglichkeit zu geben, abzufragen, ob es neue Nachrichten gibt...
			DatagramPacket dp = Util.getMessageAsDatagrammPacket(new Message(MessageType.Broadcast, new PayloadMessage("", "")), serverAddress, serverPort);
			while (true) {
				try {
					udps.receive(dp);
					Message m = Util.getMessageOutOfDatagramPacket(dp);

					switch (m.getType()) {
					case DeregisterProducer:
						PayloadProducer pp = (PayloadProducer) m.getPayload();
						System.out.println("Der Producer " + pp.getName() + " hat den Dienst eingestellt. Sie können leider keine Push-Nachrichten mehr von ihm erhalten...");
						producerList.remove(pp.getName());
						subscriptions.remove(pp.getName());
						break;

					case Broadcast:
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
