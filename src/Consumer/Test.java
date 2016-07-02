package Consumer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import Message.Message;
import Message.MessageType;
import Message.PayloadRegisterOnServer;

public class Test {

	public static void main(String[] args) throws Exception {
		ServerSocket ss = null;
		ObjectOutputStream out = null;
		ObjectInputStream in = null;
		Socket client = null;

		ss = new ServerSocket(55555);
		while (true) {
			System.out.println("Warte auf Anfrage eines Clients");
			client = ss.accept();
			System.out.println("Anfrage akzeptiert");

			out = new ObjectOutputStream(client.getOutputStream());
			in = new ObjectInputStream(client.getInputStream());

			System.out.println("Habe Streams geöffnet");

			System.out.println("Lese jetzt die Nachricht");
			Message m = (Message) in.readObject();
			Message answer = new Message(MessageType.RegisterOnServer,
					new PayloadRegisterOnServer(1234, InetAddress.getByName("228.5.6.7")));
			System.out.println("Schreibe jetzt die nachricht");
			out.writeObject(answer);
		}

	}

}
