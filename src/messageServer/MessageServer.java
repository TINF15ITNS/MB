package messageServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

import Message.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class MessageServer {

	// nicht das gleihe Objekt zurücksenden! Wird iwie die gleiche Referenz zurückgesendet, dann erkennt das der Client und nimmt das alte Objekt warum auch
	// immer und nicht das neuen mit den veränderten Variablenwerten ... Quelle Internet

	public final int portMessageServer;
	private static int numberOfCustomers = 0, numberOfProducers = 0;
	HashSet<Integer> dataConsumer;
	HashMap<Integer, ProducerMS> dataProducer;
	Scanner scanner;
	InetAddress multicastadr;

	public MessageServer(int pms) {
		dataConsumer = new HashSet<>();
		dataProducer = new HashMap<>();
		portMessageServer = pms;
		try {
			multicastadr = InetAddress.getByName("255.255.255.255");
		} catch (UnknownHostException e) {
			// tritt nicht ein, da vorgegeben
		}
	}

	/**
	 * waits for Messages from Producers or Consumers
	 */
	public void getMessages() {
		try (ServerSocket serverSo = new ServerSocket(4711)) {
			Socket clientSo = null;
			while (true) {
				clientSo = serverSo.accept();
				Thread t = new Thread(new MessageHandler(clientSo));
				t.start();
			}
		} catch (IOException e) {
			System.out.println("IOFehler beim ServerSocket");
			e.printStackTrace();
		}
	}

	class MessageHandler implements Runnable {
		Socket client;

		public MessageHandler(Socket client) {
			this.client = client;
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
					// weiterleiten an Consumer
					answer = recieveMessageFromProducer(m);
					break;
				case RegisterConsumer:
					answer = registerConsumer(m);
					break;
				case RegisterProducer:
					answer = registerProducer(m);
					break;
				case SubscribeProducers:
					answer = subscribeProducers(m);
					break;
				case UnsubscribeProducers:
					answer = unsubscribeProducers(m);
					break;
				case getProducerList:
					answer = getProducerList(m);
					break;
				case getSubscriptions:
					answer = getSubscriptions(m);
					break;
				default:
					throw new RuntimeException("Dieser Fall kann nicht eintreten");

				}

				ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
				out.writeObject(answer);

			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
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
			}

		}

		/**
		 * 
		 * @param m
		 * @return
		 */
		public Message getSubscriptions(Message m) {
			return null;
		}

		/**
		 * In this Method the MessageServer offers the available Producers
		 * 
		 * @param m
		 * @return
		 */
		private Message getProducerList(Message m) {
			// heißt er generiert ne Antwort-Message und returned diese
			return null;

		}

		/**
		 * This Method registers the consumers.
		 * 
		 * @return
		 */
		private Message registerConsumer(Message m) {
			numberOfCustomers++;
			int id = numberOfCustomers;
			dataConsumer.add(new Integer(id));
			return new Message(MessageType.RegisterConsumer, new PayloadRegisterConsumer(id, multicastadr));
		}

		/**
		 * This Method accepts the register-messages and saves the producer.
		 * 
		 * @return
		 */
		private Message registerProducer(Message m) {
			int id = numberOfProducers++;

			PayloadRegisterProducer prp = (PayloadRegisterProducer) m.getPayload();
			ProducerMS prod = new ProducerMS(id, prp.getName());
			dataProducer.put(prod.getID(), prod);

			// not implemented !!!!!!!!!!!!!!!
			return new Message(MessageType.RegisterProducer, new PayloadRegisterProducer(id, multicastadr));
		}

		/**
		 * this method forwards the messages of the Producers to the Consumers
		 * 
		 * @param m
		 * @return
		 */

		private Message recieveMessageFromProducer(Message m) {
			PayloadMessage pm = (PayloadMessage) m.getPayload();
			ProducerMS p = dataProducer.get(pm.getSenderID());
			sendMulticastMessage(p.getName() + "meldet: \n" + pm.getText());
			return new Message(MessageType.Message, null); // ????????

		}

		/**
		 * unsubscribes the Consumer, who sent the message, on the server
		 * 
		 * @param m
		 *            sended message
		 * @return answer-message
		 */
		private Message deregisterConsumer(Message m) {
			PayloadDeregisterConsumer pdc = (PayloadDeregisterConsumer) m.getPayload();
			dataConsumer.remove(pdc.getSenderID());
			return new Message(MessageType.DeregisterConsumer, null); // ???????
		}

		/**
		 * unsubscribes the producer, who sent the message, on the server
		 * 
		 * @param m
		 *            sended message
		 * @return answer-message
		 */
		private Message deregisterProducer(Message m) {
			PayloadDeregisterProducer pdp = (PayloadDeregisterProducer) m.getPayload();
			dataProducer.remove(pdp.getSenderID());
			return new Message(MessageType.DeregisterProducer, null); // ???????
		}

		/**
		 * subscribes the consumer, who sent the message, at the desired producers
		 * 
		 * @param m
		 * @return
		 */
		private Message subscribeProducers(Message m) {
			return null;

		}

		/**
		 * unsubscribes the consumer, who sent the message, at the desired producers
		 * 
		 * @param m
		 * @return
		 */
		private Message unsubscribeProducers(Message m) {
			return null;

		}

		public boolean sendMulticastMessage(String s) throws NotImplementedException {
			throw new NotImplementedException();
		}
	}

	class ProducerMS {

		private final int ID;
		private final String name;

		public ProducerMS(int id, String n) {
			ID = id;
			name = n;
		}

		/**
		 * @return the iD
		 */
		public int getID() {
			return ID;
		}

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}
	}
}
