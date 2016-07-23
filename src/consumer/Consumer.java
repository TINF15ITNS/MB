/**
 * 
 */
package consumer;

import java.io.*;
import java.net.*;
import java.util.*;
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
	MulticastSocket udpSocket = null;
	PipedReader pr;

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
			return false; // If there is no connection to the server, the consumer cannot be registered.
		}

		PayloadRegisterConsumer answerPayload = (PayloadRegisterConsumer) answer.getPayload();
		if (!answerPayload.getSuccess()) {
			registered = false;
			return false; // If the process was unsuccessful the consumer cannot be registered.
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

		pr = new PipedReader();
		Thread t = new Thread(new WaitForMessage(udpSocket));
		t.start();
		registered = answerPayload.getSuccess();
		return true;
	}

	@Override
	public HashSet<String> getProducers() {
		Message answer;
		try {
			answer = Util.sendAndGetMessage(MessageFactory.createRequestProducerListMsg(), serverAddress, serverPort);
		} catch (IOException e) {
			return null;
		}
		PayloadProducerList answerPayload = (PayloadProducerList) answer.getPayload();
		if (!answerPayload.getSuccess())
			return null;
		return (answerPayload.getProducers());
	}

	@Override
	public String[] subscribeToProducers(String[] producers) {
		if (producers == null)
			return new String[0]; // There are no producers to be subscribed to, so there are none where it was not possible

		// TODO passt das so?
		List<String> unsuccessfulProducers = new LinkedList<>();
		HashSet<String> actualProducers = getProducers();
		for (String s : producers) {
			// existiert dieser Producer?
			if (actualProducers.contains(s)) {
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
		if (producers == null)
			return new String[0]; // There are no producers to be unsubscribed from, so there are none where it was not possible

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
		if (!answerPayload.getSuccess())
			return false;

		try {
			udpSocket.leaveGroup(mcastadr);
		} catch (IOException e) {
			return false;
		}
		registered = !answerPayload.getSuccess();
		return answerPayload.getSuccess();
	}

	public boolean isRegistered() {
		return registered;
	}

	@Override
	public String getNewBroadcasts() {
		StringBuffer s = new StringBuffer("Ihre neuen Nachrichten: \n\n");
		try {
			while (pr.ready()) {
				s.append((char) pr.read());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		s.append("\n\n");
		return s.toString();
	}

	/**
	 * 
	 * The class is listening for messages from the server and prints them on the console
	 *
	 */
	private class WaitForMessage implements Runnable {
		private MulticastSocket udps;

		private PipedWriter pw;

		public WaitForMessage(MulticastSocket udps) {
			this.udps = udps;
			pw = new PipedWriter();
			try {
				pw.connect(pr);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				// kann nicht auftreten
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			DatagramPacket dp = Util.getMessageAsDatagrammPacket(MessageFactory.createBroadcastMessage("", ""), serverAddress, serverPort);
			while (true) {
				try {
					udps.receive(dp);
					Message m = Util.getMessageOutOfDatagramPacket(dp);

					switch (m.getType()) {
					case DeregisterProducer:
						PayloadProducer pp = (PayloadProducer) m.getPayload();
						pw.write("Der Producer " + pp.getName()
								+ " hat den Dienst eingestellt. Sie können leider keine Push-Nachrichten mehr von ihm erhalten...");
						subscriptions.remove(pp.getName());
						break;
					case Broadcast:
						PayloadBroadcast payload = (PayloadBroadcast) m.getPayload();
						if (subscriptions.contains(payload.getSender())) {
							pw.write("Sie haben eine neue Push-Mitteilung:");
							pw.write(payload.getSender() + " meldet: \n" + payload.getMessage());
						}
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
