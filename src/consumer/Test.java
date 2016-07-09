package consumer;

import java.io.*;
import java.net.*;

import message.*;

public class Test {

	public static void main(String[] args) throws Exception {
		ServerSocket ss = null;
		ObjectOutputStream out = null;
		ObjectInputStream in = null;
		Socket client = null;

		ss = new ServerSocket(55555);
		while (true) {

			/*
			 * Beispiel getSubscriptions: try {
			 * System.out.println("Warte auf Anfrage eines Clients"); client =
			 * ss.accept(); System.out.println("Anfrage akzeptiert"); out = new
			 * ObjectOutputStream(client.getOutputStream()); in = new
			 * ObjectInputStream(client.getInputStream());
			 * System.out.println("Habe Streams ge�ffnet");
			 * System.out.println("Lese jetzt die Nachricht");
			 * 
			 * Message m = (Message) in.readObject(); Message answer = new
			 * Message(MessageType.getSubscriptions, new
			 * PayloadSubscriptions(new String[] {"Sport","Kultur"}));
			 * System.out.println("Schreibe jetzt die nachricht");
			 * out.writeObject(answer);
			 * 
			 * } catch (Exception e) {
			 * System.out.println("Verbindung unterbrochen."); }
			 */

			/*
			 * Beispiel SubscribeProducers: try {
			 * System.out.println("Warte auf Anfrage eines Clients"); client =
			 * ss.accept(); System.out.println("Anfrage akzeptiert"); out = new
			 * ObjectOutputStream(client.getOutputStream()); in = new
			 * ObjectInputStream(client.getInputStream());
			 * System.out.println("Habe Streams ge�ffnet");
			 * System.out.println("Lese jetzt die Nachricht");
			 * 
			 * Message m = (Message) in.readObject(); PayloadSubscribeProducers
			 * p = (PayloadSubscribeProducers)m.getPayload(); for (String string
			 * : p.getToBeSubscribed()) { System.out.println("- " + string); }
			 * System.out.println("Schreibe jetzt die nachricht");
			 * out.writeObject(null);
			 * 
			 * } catch (Exception e) {
			 * System.out.println("Verbindung unterbrochen."); }
			 */

			/*
			 * Beispiel UnsubscribeProducers: try {
			 * System.out.println("Warte auf Anfrage eines Clients"); client =
			 * ss.accept(); System.out.println("Anfrage akzeptiert"); out = new
			 * ObjectOutputStream(client.getOutputStream()); in = new
			 * ObjectInputStream(client.getInputStream());
			 * System.out.println("Habe Streams ge�ffnet");
			 * System.out.println("Lese jetzt die Nachricht");
			 * 
			 * Message m = (Message) in.readObject();
			 * PayloadUnsubscribeProducers p =
			 * (PayloadUnsubscribeProducers)m.getPayload(); for (String string :
			 * p.getToBeUnsubscribed()) { System.out.println("- " + string); }
			 * System.out.println("Schreibe jetzt die nachricht");
			 * out.writeObject(null);
			 * 
			 * } catch (Exception e) {
			 * System.out.println("Verbindung unterbrochen."); }
			 */

			/*
			 * Beispiel getProducerList: try {
			 * System.out.println("Warte auf Anfrage eines Clients"); client =
			 * ss.accept(); System.out.println("Anfrage akzeptiert"); out = new
			 * ObjectOutputStream(client.getOutputStream()); in = new
			 * ObjectInputStream(client.getInputStream());
			 * System.out.println("Habe Streams ge�ffnet");
			 * System.out.println("Lese jetzt die Nachricht");
			 * 
			 * Message m = (Message) in.readObject();
			 * System.out.println("Schreibe jetzt die nachricht");
			 * out.writeObject(new Message(MessageType.getProducerList, new
			 * PayloadGetProducerList(new String[] { "Sport", "Kultur" })));
			 * 
			 * } catch (Exception e) {
			 * System.out.println("Verbindung unterbrochen."); }
			 */

			/*
			 * Beispiel: RegisterConsumer try {
			 * System.out.println("Warte auf Anfrage eines Clients"); client =
			 * ss.accept(); System.out.println("Anfrage akzeptiert"); out = new
			 * ObjectOutputStream(client.getOutputStream()); in = new
			 * ObjectInputStream(client.getInputStream());
			 * System.out.println("Habe Streams ge�ffnet");
			 * System.out.println("Lese jetzt die Nachricht");
			 * 
			 * Message m = (Message) in.readObject();
			 * System.out.println("Schreibe jetzt die nachricht");
			 * out.writeObject(new Message(MessageType.RegisterConsumer, new
			 * PayloadRegisterConsumer(1337,
			 * InetAddress.getByName("8.8.8.8"))));
			 * 
			 * } catch (Exception e) {
			 * System.out.println("Verbindung unterbrochen."); }
			 */
			
			
			
			ss.close();
		}

	}

}
