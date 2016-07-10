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
import java.util.Scanner;

import message.*;

public class MessageServer {

	// nicht das gleihe Objekt zurücksenden! Wird iwie die gleiche Referenz zurückgesendet, dann erkennt das der Client und nimmt das alte Objekt warum auch
	// immer und nicht das neuen mit den veränderten Variablenwerten ... Quelle Internet

	public final int serverPort;
	private static int numberOfCustomers = 0;
	HashSet<Integer> dataConsumer;
	HashSet<String> dataProducer;
	Scanner scanner;
	InetAddress multicastadr;

	public MessageServer(int sp) {
		dataConsumer = new HashSet<>();
		dataProducer = new HashSet<>();
		serverPort = sp;
		try {
			multicastadr = InetAddress.getByName("255.255.255.255");
		} catch (UnknownHostException e) {
			// tritt nicht ein, da vorgegeben
		}
	}

	/**
	 * waits for Messages from Producers or Consumers
	 */
	public void initialisingForwardingMessages() {
		try (ServerSocket serverSo = new ServerSocket(4711)) {
			MulticastSocket udpSocket = new MulticastSocket();
			udpSocket.setTimeToLive(1);

			Socket clientSo = null;
			while (true) {
				clientSo = serverSo.accept();
				Thread t = new Thread(new MessageHandler(clientSo, udpSocket));
				t.start();
			}
		} catch (IOException e) {
			System.out.println("IOFehler beim Erzeugen des ServerSockets oder des MulticastSockets");
			e.printStackTrace();
		}
	}

	private class MessageHandler implements Runnable {
		Socket client;
		MulticastSocket udpSocket;

		public MessageHandler(Socket client, MulticastSocket udpSocket) {
			this.client = client;
			this.udpSocket = udpSocket;
		}

		@Override
		public void run() {
			try {
				ObjectInputStream in = new ObjectInputStream(client.getInputStream());
				Message m = (Message) in.readObject();
				Message answer = null;

				switch (m.getType()) {
				case DeregisterConsumer:
					answer = deregisterConsumer(m);
					break;
				case DeregisterProducer:
					answer = deregisterProducer(m);
					break;
				case Message:
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

				ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
				out.writeObject(answer);

			} catch (ClassNotFoundException e) {
				System.out.println("Kann kein Objekt aus dem Stream lesen ... Klasse nicht auffindbar");
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (client != null) {
					try {
						client.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (udpSocket != null) {
					udpSocket.close();
				}
			}
		}

		/**
		 * In this Method the MessageServer offers the available Producers
		 * 
		 * @param m
		 *            sended message
		 * @return the response-message
		 */
		private Message getProducerList(Message m) {
			PayloadGetProducerList payload = new PayloadGetProducerList(dataProducer.toArray(new String[0]));
			return new Message(MessageType.getProducerList, payload);

		}

		/**
		 * This Method registers the consumers.
		 * 
		 * @param m
		 *            sended message
		 * @return the response-message
		 */
		private Message registerConsumer(Message m) {
			numberOfCustomers++;
			dataConsumer.add(new Integer(numberOfCustomers));
			return new Message(MessageType.RegisterConsumer, new PayloadRegisterConsumer(numberOfCustomers, multicastadr));
		}

		/**
		 * This Method accepts the register-messages and saves the producer.
		 * 
		 * @param m
		 *            sended message
		 * @return the response-message
		 */
		private Message registerProducer(Message m) {
			PayloadProducer pp = (PayloadProducer) m.getPayload();
			PayloadProducer ppresp = new PayloadProducer(pp.getName());
			if (!dataProducer.contains(pp.getName())) {
				dataProducer.add(pp.getName());
				ppresp.setSuccess();
				// TODO ich glaube das ist mit diesem Payload nicht so schön!!! mit dem aufruf setSuccess ...
			}
			return new Message(MessageType.RegisterProducer, ppresp);
		}

		/**
		 * this method forwards the messages of the Producers to the Consumers
		 * 
		 * @param m
		 *            sended message
		 * @return the response-message
		 */

		private Message receiveMessageFromProducer(Message m) {
			PayloadMessage pm = (PayloadMessage) m.getPayload();
			// schauen, ob der Absender sich beim Server auch angemeldet hat
			if (dataProducer.contains(pm.getName())) {
				DatagramPacket dp = Message.getMessageAsDatagrammPacket(
						new Message(MessageType.Message, new PayloadMessage("Server", pm.getName() + "meldet: \n" + pm.getText())), multicastadr, serverPort);
				sendMulticastMessage(dp);
				// TODO: soll bzw. was soll zuückgesendet werden
				PayloadMessage pmresp = new PayloadMessage("Server", "ok");
				return new Message(MessageType.Message, pmresp);
			}
			return new Message(MessageType.Message, null);
		}

		/**
		 * unsubscribes the Consumer, who sent the message, on the server
		 * 
		 * @param m
		 *            sended message
		 * @return the response-message
		 */
		private Message deregisterConsumer(Message m) {
			PayloadDeregisterConsumer pdc = (PayloadDeregisterConsumer) m.getPayload();
			dataConsumer.remove(pdc.getSenderID());
			// TODO: soll bzw. was soll zuückgesendet werden
			return new Message(MessageType.DeregisterConsumer, new PayloadDeregisterConsumer(0));
		}

		/**
		 * unsubscribes the producer, who sent the message, on the server
		 * 
		 * @param m
		 *            sended message
		 * @return the response-message
		 */
		private Message deregisterProducer(Message m) {
			PayloadProducer pdp = (PayloadProducer) m.getPayload();
			dataProducer.remove(pdp.getName());
			DatagramPacket dp = Message.getMessageAsDatagrammPacket(new Message(MessageType.DeregisterProducer, new PayloadProducer(pdp.getName())),
					multicastadr, serverPort);
			sendMulticastMessage(dp);
			// TODO was soll hier zurückgesendet werden
			return new Message(MessageType.DeregisterProducer, null);
		}

		public boolean sendMulticastMessage(DatagramPacket dp) {
			try {
				udpSocket.send(dp);
			} catch (IOException e) {
				System.out.println("IOFehler beim Senden der Multicast-Nachricht");
				e.printStackTrace();
			}
			return true;
		}
	}
}
