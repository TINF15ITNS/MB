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
	private InetAddress multicastAddress;
	private InetAddress serverAddress;
	private HashSet<String> subscriptions;
	MulticastSocket udpSocket;
	PipedReader pipedMessageReader;
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
		this.multicastAddress = answerPayload.getMulticastAddress();

		try {
			udpSocket = new MulticastSocket(serverPort);
			udpSocket.joinGroup(multicastAddress);
		} catch (IOException e) {
			registered = false;
			return false;
		}
		pipedMessageReader = new PipedReader();
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
		for (String producer : producers) {
			if (actualProducers.contains(producer)) {
				subscriptions.add(producer);
			} else {
				unsuccessfulProducers.add(producer);
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
		for (String producer : producers) {
			if (!subscriptions.remove(producer)) {
				list.add(producer);
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
			udpSocket.leaveGroup(multicastAddress);
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
		StringBuffer stringBuffer = new StringBuffer("");
		try {
			while (pipedMessageReader.ready()) {
				stringBuffer.append((char) pipedMessageReader.read());
			}
		} catch (IOException e) {
			// TODO überprüfen: tritt ein, wenn Pipe gebrochen/beendet, aber diese Methode kann nicht mehr aufgrufen werden, wenn PipedWriter = anderer Thread
			// geschlossen ist
			throw new RuntimeException("reading Pipe throws IOException");
		}
		return stringBuffer.toString();
	}

	@Override
	public boolean stopReceiving() {
		return messageWaiter.stopThread();
	}

	@Override
	public boolean hasNewMessages() {
		try {
			return pipedMessageReader.ready();
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
		private PipedWriter pipedMessageWriter;
		private boolean isRunning = true;

		public WaitForMessage(MulticastSocket udpSocket) {
			this.udpSocket = udpSocket;
			pipedMessageWriter = new PipedWriter();
			try {
				pipedMessageWriter.connect(pipedMessageReader);
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
			DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length, serverAddress, serverPort);
			while (isRunning) {
				try {
					udpSocket.receive(datagramPacket);
					Message recievedMessage = Util.getMessageOutOfDatagramPacket(datagramPacket);

					switch (recievedMessage.getType()) {
					case DeregisterProducer:
						if (recievedMessage.getType() != MessageType.DeregisterProducer || !(recievedMessage.getPayload() instanceof PayloadProducer)) {
							throw new RuntimeException("Wrong Payload");
						}
						PayloadProducer payloadProducer = (PayloadProducer) recievedMessage.getPayload();

						if (subscriptions.contains(payloadProducer.getName())) {
							pipedMessageWriter.write("Der Producer " + payloadProducer.getName() + " hat den Dienst eingestellt. Sie können leider keine Push-Nachrichten mehr von ihm erhalten...");
							subscriptions.remove(payloadProducer.getName());
						}
						break;
					case Broadcast:
						if (recievedMessage.getType() != MessageType.Broadcast || !(recievedMessage.getPayload() instanceof PayloadBroadcast)) {
							throw new RuntimeException("Wrong Payload");
						}
						PayloadBroadcast payload = (PayloadBroadcast) recievedMessage.getPayload();

						if (subscriptions.contains(payload.getSender())) {
							pipedMessageWriter.write("\nSie haben eine neue Push-Mitteilung, " + payload.getSender() + " meldet: \n" + payload.getMessage() + "\n");
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
