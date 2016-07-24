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
	MulticastSocket udpSocket;
	PipedReader pr;
	private WaitForMessage messageWaiter;

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
		this.serverAddress = InetAddress.getByName(address.trim());
		if (!Util.testConnection(serverAddress, serverPort, 1000))
			throw new IOException("There ist no server on the specified address");

	}

	@Override
	public boolean registerOnServer() {
		Message answer;

		try {
			answer = Util.sendAndGetMessage(MessageFactory.createRegisterConsumerMsg(), serverAddress, serverPort);
		} catch (IOException e) {
			registered = false;
			return false; // If there is no connection to the server, the consumer cannot be registered.
		}

		if (answer.getType() != MessageType.RegisterConsumer || !(answer.getPayload() instanceof PayloadRegisterConsumer)) {
			throw new RuntimeException("Wrong Payload");
		}
		PayloadRegisterConsumer answerPayload = (PayloadRegisterConsumer) answer.getPayload();
		if (!answerPayload.getSuccess()) {
			registered = false;
			return false; // If the process was unsuccessful the consumer cannot be registered.
		}
		this.consumerID = answerPayload.getId();
		this.mcastadr = answerPayload.getMulticastAddress();

		try {
			udpSocket = new MulticastSocket(serverPort);
			udpSocket.joinGroup(mcastadr);
		} catch (IOException e) {
			registered = false;
			return false;
		}
		pr = new PipedReader();
		messageWaiter = new WaitForMessage(udpSocket);
		Thread t = new Thread(messageWaiter);
		t.start();
		registered = answerPayload.getSuccess();
		return true;
	}

	@Override
	public HashSet<String> getProducers() {
		Message answer;
		try {
			answer = Util.sendAndGetMessage(MessageFactory.createProducerListMsg(), serverAddress, serverPort);
		} catch (IOException e) {
			return null;
		}

		if (answer.getType() != MessageType.getProducerList || !(answer.getPayload() instanceof PayloadProducerList)) {
			throw new RuntimeException("Wrong Payload");
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

		List<String> unsuccessfulProducers = new LinkedList<>();
		HashSet<String> actualProducers = getProducers();
		for (String s : producers) {
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
			answer = Util.sendAndGetMessage(MessageFactory.createDeregisterConsumerMsg(consumerID), serverAddress, serverPort);
		} catch (IOException e) {
			return false;
		}

		if (answer.getType() != MessageType.DeregisterConsumer || !(answer.getPayload() instanceof PayloadDeregisterConsumer)) {
			throw new RuntimeException("Wrong Payload");
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

	@Override
	public boolean isRegistered() {
		return registered;
	}

	@Override
	public String getNewBroadcasts() {
		StringBuffer s = new StringBuffer("");
		try {
			while (pr.ready()) {
				s.append((char) pr.read());
			}
		} catch (IOException e) {
			// TODO überprüfen: tritt ein, wenn Pipe gebrochen/beendet, aber diese Methode kann nicht mehr aufgrufen werden, wenn PipedWriter = anderer Thread
			// geschlossen ist
			throw new RuntimeException("reading Pipe throws IOException");
		}
		return s.toString();
	}

	@Override
	public boolean stopReceiving() {
		return messageWaiter.stopThread();
	}
	
	@Override
	public boolean hasNewMessages() {
		try {
			return pr.ready();
		} catch (IOException e) {
			return false;
		}
	}

	/**
	 * 
	 * The class is listening for messages from the server and writes them into
	 * the Pipe
	 *
	 */
	private class WaitForMessage implements Runnable {
		private MulticastSocket udpSocket;
		private PipedWriter pw;
		private boolean isRunning = true;

		public WaitForMessage(MulticastSocket udpSocket) {
			this.udpSocket = udpSocket;
			pw = new PipedWriter();
			try {
				pw.connect(pr);
			} catch (IOException e) {
				// TODO überprüfen: kann nicht auftreten, da PipedReader nicht schon verheiratet sein kann
				throw new RuntimeException("Pipe is alredy connected");
			}
		}

		/**
		 * Prevents the thread in run() to do another iteration of its action.
		 * 
		 * @return true if the thread has been stopped, false if the thread was
		 *         already stopped
		 */
		public boolean stopThread() {
			if (isRunning) {
				isRunning = false;
				return true;
			} else
				return false;
		}

		@Override
		public void run() {
			byte[] buffer = new byte[65508];// max size of a DatagramPacket
			DatagramPacket dp = new DatagramPacket(buffer, buffer.length, serverAddress, serverPort);
			while (isRunning) {
				try {
					udpSocket.receive(dp);
					Message m = Util.getMessageOutOfDatagramPacket(dp);

					switch (m.getType()) {
					case DeregisterProducer:
						if (m.getType() != MessageType.DeregisterProducer || !(m.getPayload() instanceof PayloadProducer)) {
							throw new RuntimeException("Wrong Payload");
						}
						PayloadProducer pp = (PayloadProducer) m.getPayload();

						if (subscriptions.contains(pp.getName())) {
							pw.write("Der Producer " + pp.getName() + " hat den Dienst eingestellt. Sie können leider keine Push-Nachrichten mehr von ihm erhalten...");
							subscriptions.remove(pp.getName());
						}
						break;
					case Broadcast:
						if (m.getType() != MessageType.Broadcast || !(m.getPayload() instanceof PayloadBroadcast)) {
							throw new RuntimeException("Wrong Payload");
						}
						PayloadBroadcast payload = (PayloadBroadcast) m.getPayload();

						if (subscriptions.contains(payload.getSender())) {
							pw.write("\nSie haben eine neue Push-Mitteilung, " + payload.getSender() + " meldet: \n" + payload.getMessage() + "\n");
						}
						break;

					default:
						throw new RuntimeException("Wrong MessageType");
					}
				} catch (IOException e) {
					System.out.println("Fehler beim Bearbeiten der erhaltenen UDP-Message");
					e.printStackTrace();
				}
			}
		}
	}
}
