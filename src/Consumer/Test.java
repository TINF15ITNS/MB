package Consumer;

import java.io.*;
import java.net.*;
import Message.*;

public class Test {

	public static void main(String[] args) throws Exception {
		ServerSocket ss = null;
		ObjectOutputStream out = null;
		ObjectInputStream in = null;
		Socket client = null;

		ss = new ServerSocket(55555);
		while (true) {
			
			/* Beispiel Liste Subscriptions:
			try {
				System.out.println("Warte auf Anfrage eines Clients");
				client = ss.accept();
				System.out.println("Anfrage akzeptiert");

				out = new ObjectOutputStream(client.getOutputStream());
				in = new ObjectInputStream(client.getInputStream());

				System.out.println("Habe Streams ge�ffnet");

				System.out.println("Lese jetzt die Nachricht");
				Message m = (Message) in.readObject();
				Message answer = new Message(MessageType.getSubscriptions,
						new PayloadSubscriptions(new String[] {"Sport","Kultur"}));
				System.out.println("Schreibe jetzt die nachricht");
				out.writeObject(answer);
			} catch (Exception e) {
				System.out.println("Verbindung unterbrochen.");
			}
			*/

		}

	}

}
