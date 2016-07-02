package MessageServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

import Message.Message;

public class MessageServer {

	// nicht das gleihe Objekt zurücksenden! Wird iwie die gleiche Referenz zurückgesendet, dann erkennt das der Client und nimmt das alte Objekt warum auch
	// immer und nicht das neuen mit den veränderten Variablenwerten ... Quelle Internet

	public final int portMessageServer;
	HashMap<Integer, Customer> hashMapProducer, hashMapConsumer;
	Scanner scanner;

	public MessageServer(int pms) {
		hashMapProducer = new HashMap<>();
		hashMapConsumer = new HashMap<>();
		portMessageServer = pms;

	}

	// Oder das hier in der MainMethode in eigene threads auslagern
	// da hab ich mir noch keine gedanken drüber gemacht
	/**
	 * waits for Messages from Producers or Consumers
	 */
	public void getMessages() {
		// diese Methode hält den einen Port offen, wo auf nachrichten der konsumenten und Produzenten gehört wird
		// Casted diese dann nach message und schaut dann was sie damit machen soll und ruft die jeweilige Methode auf

		// hier ist dann nen ServerSocket, dass nen neuen thread startet, wenn eine Anfrage kommt.
		// wie Skript Folie 37 E

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

				/*
				 * RegisterConsumer, DeregisterConsumer, SubscribeProducers, UnsubscribeProducers, getProducerList, getSubscriptions, Message, RegisterProducer,
				 * DeregisterProducer,
				 */
				Message answer = null;
				switch (m.getType()) {
				case DeregisterConsumer:
					answer = deregisterConsumer(m);
					break;
				case DeregisterProducer:
					answer = deregisterProducer(m);
					break;
				case Message:
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
			// kommt ne registrierungsanfrage, dann wird hier ne id erzeugt und als Message zurückgesendet
			// und es wird ein ConsumerMS Objekt erzeugt ( siehe Interface Customer) und in der HashMap gespeichert
			// Key Value ist die id
			// und erzeugte id und die MulticastAdresse werden im neuen Message-Objekt gespeichert und zurückgegeben.
			return null;
		}

		/**
		 * This Method accepts the register-messages and saves the producer.
		 * 
		 * @return
		 */
		private Message registerProducer(Message m) {
			// kommt ne registrierungsanfrage, dann wird hier ne id erzeugt und als Message zurückgesendet
			// und es wird ein ConsumerMS Objekt erzeugt ( siehe Interface Customer) und in der HashMap gespeichert
			// Key Value ist die id
			// und erzeugte id und die MulticastAdresse werden im neuen Message-Objekt gespeichert und zurückgegeben.
			return null;
		}

		/**
		 * this method forwards the messages of the Producers to the Consumers
		 * 
		 * @param m
		 * @return
		 */
		private Message recieveMessageFromProducer(Message m) {
			// die große Frage: gibt es eine MulticastGruppe oder emhrere?
			return null;

		}

		/**
		 * unsubscribes the Consumer, who sent the message, on the server
		 * 
		 * @param m
		 *            sended message
		 * @return answer-message
		 */
		private Message deregisterConsumer(Message m) {
			return null;
		}

		/**
		 * unsubscribes the producer, who sent the message, on the server
		 * 
		 * @param m
		 *            sended message
		 * @return answer-message
		 */
		private Message deregisterProducer(Message m) {
			return null;
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
	}
}
