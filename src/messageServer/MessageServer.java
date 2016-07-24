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

	@Override
	public void respondOnMessages() {
		try (ServerSocket serverSo = new ServerSocket(serverPort); MulticastSocket udpSocket = new MulticastSocket(serverPort);) {

			udpSocket.setTimeToLive(1);

			Socket clientSo = null;
			while (true) {
				clientSo = serverSo.accept();
				Thread t = new Thread(new MessageHandler(clientSo, udpSocket));
				t.start();
			}
		} catch (IOException e) {
			throw new RuntimeException("IOFehler beim Erzeugen des ServerSockets oder des MulticastSockets");
		}
	}

	private class MessageHandler implements Runnable {
		private Socket s;
		private MulticastSocket udpSocket;

		public MessageHandler(Socket client, MulticastSocket udpSocket) {
			this.s = client;
			this.udpSocket = udpSocket;
		}

		@Override
		public void run() {
			try (Socket client = s;
					ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
					ObjectInputStream in = new ObjectInputStream(client.getInputStream());) {

				Message m = (Message) in.readObject();
				Message answer = null;

				switch (m.getType()) {
				case DeregisterConsumer:
					answer = deregisterConsumer(m);
					break;
				case DeregisterProducer:
					answer = deregisterProducer(m);
					break;
				case Broadcast:
					answer = receiveMessageFromProducer(m);
					break;
				case RegisterConsumer:
					answer = registerConsumer(m);
					break;
				case RegisterProducer:
					answer = registerProducer(m);
					break;
				case getProducerList:
					answer = getProducerList(m);
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
		 * @param m
		 *            sent message
		 * @return response-message
		 */
		private Message getProducerList(Message m) {
			// System.out.println("Sende Produzentenliste an " + s.getRemoteSocketAddress().toString());
			System.out.println("Produzentenliste wurde angefordert");
			return MessageFactory.createProducerListMsg(dataProducer, true);

		}

		/**
		 * This Method registers the consumers.
		 * 
		 * @param m
		 *            sent message
		 * @return response-message
		 */
		private Message registerConsumer(Message m) {
			numberOfCustomers++;
			dataConsumer.add(new Integer(numberOfCustomers));
			System.out.println("Neuanmeldung Consumer -> neue ID: " + numberOfCustomers);
			return MessageFactory.createRegisterConsumerMsg(numberOfCustomers, mcastadr, true);
		}

		/**
		 * This method accepts the register-messages and saves the producer.
		 * 
		 * @param m
		 *            sent message
		 * @return response-message
		 */
		private Message registerProducer(Message m) {
			if (m.getType() != MessageType.RegisterProducer || !(m.getPayload() instanceof PayloadProducer)) {
				throw new RuntimeException("Wrong Payload or MessageType");
			}
			PayloadProducer pp = (PayloadProducer) m.getPayload();

			if (!dataProducer.contains(pp.getName())) {
				dataProducer.add(pp.getName());
				System.out.println("Neuanmeldung Producer: " + pp.getName());
				return MessageFactory.createRegisterProducerMsg(pp.getName(), true);
			}
			System.out.println("versuchte Neuanmeldung Producer mit schon vorhandenem Namen: " + pp.getName());
			return MessageFactory.createRegisterProducerMsg(pp.getName(), false);
		}

		/**
		 * This method forwards the messages of the producers to the consumers.
		 * 
		 * @param m
		 *            sent message
		 * @return response-message
		 */

		private Message receiveMessageFromProducer(Message m) {
			if (m.getType() != MessageType.Broadcast || !(m.getPayload() instanceof PayloadBroadcast)) {
				throw new RuntimeException("Wrong Payload or MessageType");
			}
			PayloadBroadcast pm = (PayloadBroadcast) m.getPayload();

			if (dataProducer.contains(pm.getSender())) {
				DatagramPacket dp = Util.getMessageAsDatagrammPacket(MessageFactory.createBroadcastMessage(pm.getSender(), pm.getMessage()), mcastadr,
						serverPort);
				sendMulticastMessage(dp);
				System.out.println("Broadcastmessage von " + pm.getSender() + " erhalten und weitergeleitet");
				return MessageFactory.createBroadcastMessage("Server", true);
			}
			System.out.println("Broadcastmessage von noch nicht registriertem Producer " + pm.getSender() + " erhalten -> nicht weitergeleitet");
			return MessageFactory.createBroadcastMessage("Server", false);
		}

		/**
		 * Removes the Consumer from the list of subscriptions on the Server
		 * 
		 * @param m
		 *            sent message
		 * @return response-message
		 */
		private Message deregisterConsumer(Message m) {
			if (m.getType() != MessageType.DeregisterConsumer || !(m.getPayload() instanceof PayloadDeregisterConsumer)) {
				throw new RuntimeException("Wrong Payload or MessageType");
			}
			PayloadDeregisterConsumer pdc = (PayloadDeregisterConsumer) m.getPayload();

			if (dataConsumer.remove(pdc.getID())) {
				System.out.println("Consumer abgemeldet mit ID: " + pdc.getID());
				return MessageFactory.createDeregisterConsumerMsg(true);
			} else {
				System.out.println("Versuchte Abmeldung von Consumer mit der ID: " + pdc.getID() + " obwohl noch nicht registriert");
				return MessageFactory.createDeregisterConsumerMsg(false);
			}
		}

		/**
		 * removes the producer, who sent the message, from the list of producers
		 * 
		 * @param m
		 *            sent message
		 * @return response-message, Payload-attribute success is true, if the operation was successful
		 */
		private Message deregisterProducer(Message m) {
			if (m.getType() != MessageType.DeregisterProducer || !(m.getPayload() instanceof PayloadProducer)) {
				throw new RuntimeException("Wrong Payload or MessageType");
			}
			PayloadProducer pdp = (PayloadProducer) m.getPayload();

			if (dataProducer.remove(pdp.getName())) {
				DatagramPacket dp = Util.getMessageAsDatagrammPacket(MessageFactory.createDeregisterProducerMsg(pdp.getName()), mcastadr, serverPort);
				sendMulticastMessage(dp);
				System.out.println("Abmeldung Producer mit Namen " + pdp.getName());
				return MessageFactory.createDeregisterProducerMsg(pdp.getName(), true);
			} else {
				System.out.println("Versuchte Abmeldung von Producer mit Namen " + pdp.getName() + " obwohl nocht nicht registriert");
				return MessageFactory.createDeregisterProducerMsg(pdp.getName(), false);
			}
		}

		/**
		 * forwards the message to the consumers
		 * 
		 * @param dp
		 *            the message-object as DatagramPacket
		 */
		private void sendMulticastMessage(DatagramPacket dp) {
			try {
				udpSocket.send(dp);
			} catch (IOException e) {
				// TODO überprüfen: kann eigentlich nicht auftreten, da alle Adressen vorgegeben sind
				throw new RuntimeException("Problem beim Senden der MulticastMessage");
			}
		}
	}
}
