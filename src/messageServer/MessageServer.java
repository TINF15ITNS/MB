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

import message.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class MessageServer {

	// nicht das gleihe Objekt zurücksenden! Wird iwie die gleiche Referenz zurückgesendet, dann erkennt das der Client und nimmt das alte Objekt warum auch
	// immer und nicht das neuen mit den veränderten Variablenwerten ... Quelle Internet

	public final int portMessageServer;
	private static int numberOfCustomers = 0;
	HashSet<Integer> dataConsumer;
	HashSet<String> dataProducer;
	Scanner scanner;
	InetAddress multicastadr;

	public MessageServer(int pms) {
		dataConsumer = new HashSet<>();
		dataProducer = new HashSet<>();
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
					answer = receiveMessageFromProducer(m);
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
		 *            sended message
		 * @return
		 */
		private Message getProducerList(Message m) {
			// heißt er generiert ne Antwort-Message und returned diese
			PayloadGetProducerList payload = new PayloadGetProducerList(dataProducer.toArray(new String[0]));
			return new Message(MessageType.getProducerList, payload);

		}

		/**
		 * This Method registers the consumers.
		 * 
		 * @param m
		 *            sended message
		 * @return
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
		 * @return
		 */
		private Message registerProducer(Message m) {

			PayloadProducer pp = (PayloadProducer) m.getPayload();
			PayloadProducer ppresp = new PayloadProducer(pp.getName());
			// falls Name schon vorhanden, wird Success nicht auf true gesetzte
			if (!dataProducer.contains(pp.getName())) {
				dataProducer.add(pp.getName());
				ppresp.setSuccess();
			}
			return new Message(MessageType.RegisterProducer, ppresp);
		}

		/**
		 * this method forwards the messages of the Producers to the Consumers
		 * 
		 * @param m
		 *            sended message
		 * @return
		 */

		private Message receiveMessageFromProducer(Message m) {
			PayloadMessage pm = (PayloadMessage) m.getPayload();
			// schauen, ob der Absender sich beim Server auch angemeldet hat
			if (dataProducer.contains(pm.getName())) {
				sendMulticastMessage(pm.getName() + "meldet: \n" + pm.getText());
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
		 * @return answer-message
		 */
		private Message deregisterConsumer(Message m) {
			PayloadDeregisterConsumer pdc = (PayloadDeregisterConsumer) m.getPayload();
			dataConsumer.remove(pdc.getSenderID());
			// TODO: soll bzw. was soll zuückgesendet werden
			return new Message(MessageType.DeregisterConsumer, null);
		}

		/**
		 * unsubscribes the producer, who sent the message, on the server
		 * 
		 * @param m
		 *            sended message
		 * @return answer-message
		 */
		private Message deregisterProducer(Message m) {
			PayloadProducer pdp = (PayloadProducer) m.getPayload();
			dataProducer.remove(pdp.getName());
			return new Message(MessageType.DeregisterProducer, null); // ???????
		}

		/**
		 * subscribes the consumer, who sent the message, at the desired producers
		 * 
		 * @param m
		 *            sended message
		 * @return
		 */
		private Message subscribeProducers(Message m) {
			return null;

		}

		/**
		 * unsubscribes the consumer, who sent the message, at the desired producers
		 * 
		 * @param m
		 *            sended message
		 * @return
		 */
		private Message unsubscribeProducers(Message m) {
			return null;

		}

		public boolean sendMulticastMessage(String s) {
			return false;
		}
	}
}
