/**
 * 
 */
package messageServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashSet;

import message.Message;
import message.MessageFactory;
import message.MessageType;
import message.PayloadBroadcast;
import message.PayloadDeregisterConsumer;
import message.PayloadProducer;
import message.Util;

/**
 * 
 * @author Nikolai Seip, Sebastian Mattheis, Fabian Hinz
 *
 */
public class MessageServer implements MessageServerIF {

	private final int serverPort;
	private static int numberOfCustomers = 0;
	private HashSet<Integer> dataConsumer;
	private HashSet<String> dataProducer;
	private InetAddress mcastadr;

	public MessageServer(int serverPort) {
		dataConsumer = new HashSet<>();
		dataProducer = new HashSet<>();
		this.serverPort = serverPort;
		try {
			mcastadr = InetAddress.getByName("225.225.225.225");
		} catch (UnknownHostException e) {
			throw new RuntimeException("Multicast-Adresse ist fehlerhaft");
		}
	}

	@Override
	public int getServerPort() {
		return serverPort;
	}

	/**
	 * waits for messages from producers or consumers
	 */
	// TODO MessageServer beendbar machen
	@Override
	public void respondOnMessages() {
		try (ServerSocket serverSo = new ServerSocket(serverPort); MulticastSocket udpSocket = new MulticastSocket(serverPort);) {

			udpSocket.setTimeToLive(1);

			Socket clientSo = null;
			while (true) {
				clientSo = serverSo.accept();
				// System.out.println("Verbindung mit" + clientSo.getRemoteSocketAddress().toString() + " aufgebaut. ");
				// System.out.println("TCP-Verbindungsanfrage");
				Thread t = new Thread(new MessageHandler(clientSo, udpSocket));
				t.start();
			}
		} catch (IOException e) {
			System.out.println("IOFehler beim Erzeugen des ServerSockets oder des MulticastSockets");
			e.printStackTrace();
		}
	}

	private class MessageHandler implements Runnable {
		private Socket socket;
		private MulticastSocket udpSocket;

		public MessageHandler(Socket client, MulticastSocket udpSocket) {
			this.socket = client;
			this.udpSocket = udpSocket;
		}

		@Override
		public void run() {
			try (Socket client = socket; ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream()); ObjectInputStream in = new ObjectInputStream(client.getInputStream());) {

				Message message = (Message) in.readObject();
				Message answer = null;

				switch (message.getType()) {
				case DeregisterConsumer:
					answer = deregisterConsumer(message);
					break;
				case DeregisterProducer:
					answer = deregisterProducer(message);
					break;
				case Broadcast:
					answer = receiveMessageFromProducer(message);
					break;
				case RegisterConsumer:
					answer = registerConsumer(message);
					break;
				case RegisterProducer:
					answer = registerProducer(message);
					break;
				case getProducerList:
					answer = getProducerList(message);
					break;
				default:
					throw new RuntimeException("Invalid message type");

				}
				out.writeObject(answer);
				out.flush();

			} catch (ClassNotFoundException e) {
				System.out.println("Kann kein Objekt aus dem Stream lesen ... Klasse nicht auffindbar");
				e.printStackTrace();
			} catch (IOException e) {
			}

		}

		/**
		 * In this method the MessageServer offers the available producers
		 * 
		 * @param message
		 *            sent message
		 * @return response-message
		 */
		private Message getProducerList(Message message) {
			// System.out.println("Sende Produzentenliste an " + s.getRemoteSocketAddress().toString());
			System.out.println("Produzentenliste wurde angefordert");
			return MessageFactory.createProducerListMsg(dataProducer, true);

		}

		/**
		 * This Method registers the consumers.
		 * 
		 * @param message
		 *            sent message
		 * @return response-message
		 */
		private Message registerConsumer(Message message) {
			numberOfCustomers++;
			dataConsumer.add(new Integer(numberOfCustomers));
			System.out.println("Neuanmeldung Consumer -> neue ID: " + numberOfCustomers);
			;
			return MessageFactory.createRegisterConsumerMsg(numberOfCustomers, mcastadr, true);
		}

		/**
		 * This method accepts the register-messages and saves the producer.
		 * 
		 * @param message
		 *            sent message
		 * @return response-message
		 */
		private Message registerProducer(Message message) {
			if (message.getType() != MessageType.RegisterProducer || !(message.getPayload() instanceof PayloadProducer)) {
				throw new RuntimeException("Wrong Payload or MessageType");
			}
			PayloadProducer payload = (PayloadProducer) message.getPayload();

			if (!dataProducer.contains(payload.getName())) {
				dataProducer.add(payload.getName());
				System.out.println("Neuanmeldung Producer: " + payload.getName());
				return MessageFactory.createRegisterProducerMsg(payload.getName(), true);
			}
			System.out.println("versuchte Neuanmeldung Producer mit schon vorhandenem Namen: " + payload.getName());
			return MessageFactory.createRegisterProducerMsg(payload.getName(), false);
		}

		/**
		 * This method forwards the messages of the producers to the consumers.
		 * 
		 * @param message
		 *            sent message
		 * @return response-message
		 */

		private Message receiveMessageFromProducer(Message message) {
			if (message.getType() != MessageType.Broadcast || !(message.getPayload() instanceof PayloadBroadcast)) {
				throw new RuntimeException("Wrong Payload or MessageType");
			}
			PayloadBroadcast payload = (PayloadBroadcast) message.getPayload();

			if (dataProducer.contains(payload.getSender())) {
				DatagramPacket dp = Util.getMessageAsDatagrammPacket(MessageFactory.createBroadcastMessage(payload.getSender(), payload.getMessage()), mcastadr, serverPort);
				sendMulticastMessage(dp);
				System.out.println("Broadcastmessage von " + payload.getSender() + " erhalten und weitergeleitet");
				return MessageFactory.createBroadcastMessage("Server", true);
			}
			System.out.println("Broadcastmessage von noch nicht registriertem Producer " + payload.getSender() + " erhalten -> nicht weitergeleitet");
			return MessageFactory.createBroadcastMessage("Server", false);
		}

		/**
		 * Removes the Consumer from the list of subscriptions on the Server
		 * 
		 * @param message
		 *            sent message
		 * @return response-message
		 */
		private Message deregisterConsumer(Message message) {
			if (message.getType() != MessageType.DeregisterConsumer || !(message.getPayload() instanceof PayloadDeregisterConsumer)) {
				throw new RuntimeException("Wrong Payload or MessageType");
			}
			PayloadDeregisterConsumer payload = (PayloadDeregisterConsumer) message.getPayload();

			if (dataConsumer.remove(payload.getID())) {
				System.out.println("Consumer abgemeldet mit ID: " + payload.getID());
				return MessageFactory.createDeregisterConsumerMsg(true);
			} else {
				System.out.println("Versuchte Abmeldung von Consumer mit der ID: " + payload.getID() + " obwohl noch nicht registriert");
				return MessageFactory.createDeregisterConsumerMsg(false);
			}
		}

		/**
		 * removes the producer, who sent the message, from the list of
		 * producers
		 * 
		 * @param message
		 *            sent message
		 * @return response-message, Payload-attribute success is true, if the
		 *         operation was successful
		 */
		private Message deregisterProducer(Message message) {
			if (message.getType() != MessageType.DeregisterProducer || !(message.getPayload() instanceof PayloadProducer)) {
				throw new RuntimeException("Wrong Payload or MessageType");
			}
			PayloadProducer payload = (PayloadProducer) message.getPayload();

			if (dataProducer.remove(payload.getName())) {
				DatagramPacket datagramPacket = Util.getMessageAsDatagrammPacket(MessageFactory.createDeregisterProducerMsg(payload.getName()), mcastadr, serverPort);
				sendMulticastMessage(datagramPacket);
				System.out.println("Abmeldung Producer mit Namen " + payload.getName());
				return MessageFactory.createDeregisterProducerMsg(payload.getName(), true);
			} else {
				System.out.println("Versuchte Abmeldung von Producer mit Namen " + payload.getName() + " obwohl nocht nicht registriert");
				return MessageFactory.createDeregisterProducerMsg(payload.getName(), false);
			}
		}

		/**
		 * forwards the message to the consumers
		 * 
		 * @param datagramPacket
		 *            the message-object as DatagramPacket
		 */
		private void sendMulticastMessage(DatagramPacket datagramPacket) {
			try {
				udpSocket.send(datagramPacket);
			} catch (IOException e) {
				// TODO überprüfen: kann eigentlich nicht auftreten, da alle Adressen vorgegeben sind
				throw new RuntimeException("Problem beim Senden der MulticastMessage");
			}
		}
	}
}
