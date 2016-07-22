/**
 * 
 */
package messageServer;

import java.io.*;
import java.net.*;
import java.util.HashSet;
import message.*;

/**
 * 
 * @author Nikolai Seip, Sebastian Mattheis, Fabian Hinz
 *
 */
public class MessageServer implements MessageServerIF {

	private final int serverPort;
	private static int numberOfCustomers = 0;
	HashSet<Integer> dataConsumer;
	HashSet<String> dataProducer;
	InetAddress multicastadr;

	public MessageServer(int serverPort) {
		dataConsumer = new HashSet<>();
		dataProducer = new HashSet<>();
		this.serverPort = serverPort;
		try {
			multicastadr = InetAddress.getByName("225.225.225.225");
		} catch (UnknownHostException e) {
			// tritt nicht ein, da vorgegeben
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
		try (ServerSocket serverSo = new ServerSocket(55555); MulticastSocket udpSocket = new MulticastSocket();) {

			udpSocket.setTimeToLive(1);

			Socket clientSo = null;
			while (true) {
				clientSo = serverSo.accept();
				System.out.println("Verbindung aufgebaut");
				Thread t = new Thread(new MessageHandler(clientSo, udpSocket));
				t.start();
			}
		} catch (IOException e) {
			System.out.println("IOFehler beim Erzeugen des ServerSockets oder des MulticastSockets");
			e.printStackTrace();
		}
	}

	private class MessageHandler implements Runnable {
		Socket s;
		MulticastSocket udpSocket;

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
					throw new RuntimeException("Dieser Fall kann nicht eintreten");

				}
				System.out.println("Antworte nun");
				out.writeObject(answer);

			} catch (ClassNotFoundException e) {
				System.out.println("Kann kein Objekt aus dem Stream lesen ... Klasse nicht auffindbar");
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		/**
		 * In this method the MessageServer offers the available producers
		 * 
		 * @param m
		 *            sended message
		 * @return response-message
		 */
		private Message getProducerList(Message m) {
			return new Message(MessageType.getProducerList, new PayloadGetProducerList(dataProducer.toArray(new String[0]), true));

		}

		/**
		 * This Method registers the consumers.
		 * 
		 * @param m
		 *            sended message
		 * @return response-message
		 */
		private Message registerConsumer(Message m) {
			numberOfCustomers++;
			dataConsumer.add(new Integer(numberOfCustomers));
			return new Message(MessageType.RegisterConsumer, new PayloadRegisterConsumer(numberOfCustomers, multicastadr, true));
		}

		/**
		 * This method accepts the register-messages and saves the producer.
		 * 
		 * @param m
		 *            sent message
		 * @return response-message
		 */
		private Message registerProducer(Message m) {
			PayloadProducer pp = (PayloadProducer) m.getPayload();
			PayloadProducer ppresp = new PayloadProducer(pp.getName());
			if (!dataProducer.contains(pp.getName())) {
				dataProducer.add(pp.getName());
				ppresp.setSuccess(true);
			}
			return new Message(MessageType.RegisterProducer, ppresp);
		}

		/**
		 * This method forwards the messages of the producers to the consumers.
		 * 
		 * @param m
		 *            sent message
		 * @return response-message
		 */

		private Message receiveMessageFromProducer(Message m) {
			PayloadBroadcast pm = (PayloadBroadcast) m.getPayload();
			// schauen, ob der Absender sich beim Server auch angemeldet hat
			if (dataProducer.contains(pm.getSender())) {
				DatagramPacket dp = Util.getMessageAsDatagrammPacket(new Message(MessageType.Broadcast, pm), multicastadr, serverPort);
				sendMulticastMessage(dp);
				return new Message(MessageType.Broadcast, new PayloadBroadcast("Server", null, true));
			}
			return new Message(MessageType.Broadcast, new PayloadBroadcast("Server", null, false));
		}

		/**
		 * unsubscribes the Consumer, who sent the message, on the server
		 * 
		 * @param m
		 *            sended message
		 * @return response-message
		 */
		private Message deregisterConsumer(Message m) {
			PayloadDeregisterConsumer pdc = (PayloadDeregisterConsumer) m.getPayload();
			// if the removing-operation
			if (dataConsumer.remove(pdc.getID())) {
				return new Message(MessageType.DeregisterConsumer, new PayloadDeregisterConsumer(true));
			} else {
				return new Message(MessageType.DeregisterConsumer, new PayloadDeregisterConsumer(false));
			}

		}

		/**
		 * unsubscribes the producer, who sent the message, on the server
		 * 
		 * @param m
		 *            sended message
		 * @return response-message, Payload-attribut success is true, if the operation was successful
		 */
		private Message deregisterProducer(Message m) {
			PayloadProducer pdp = (PayloadProducer) m.getPayload();
			PayloadProducer answerPayload = new PayloadProducer(pdp.getName());
			if (dataProducer.remove(pdp.getName())) {
				DatagramPacket dp = Util.getMessageAsDatagrammPacket(MessageFactory.createDeregisterProducerMsg(pdp.getName()), multicastadr, serverPort);
				sendMulticastMessage(dp);
				answerPayload.setSuccess(true);
				return new Message(MessageType.DeregisterProducer, answerPayload);
			} else {
				return new Message(MessageType.DeregisterProducer, answerPayload);
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
				System.out.println("IOFehler beim Senden der Multicast-Nachricht");
				e.printStackTrace();
			}
		}
	}
}
