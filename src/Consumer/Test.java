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

	public static void main(String[] args) {
		ServerSocket ss = null;
		ObjectOutputStream out = null;
		ObjectInputStream in = null;
		Socket client = null;
		try {
			ss = new ServerSocket(55555);
			while (true) {
				System.out.println("Warte auf Anfrage eines Clients");
				client = ss.accept();
				System.out.println("Anfrage akzeptiert");
				in = new ObjectInputStream(client.getInputStream());
				out = new ObjectOutputStream(client.getOutputStream());
				out.flush();

				System.out.println("Habe Streams geöffnet");

				System.out.println("Lese jetzt die Nachricht");
				Message m = (Message) in.readObject();
				Message answer = new Message(MessageType.RegisterOnServer, new PayloadRegisterOnServer(1234, InetAddress.getByName("228.5.6.7")));
				System.out.println("Schreibe jetzt die nachricht");
				out.writeObject(answer);
			}
		} catch (ClassNotFoundException e) {
			System.out.println("ClassNotFoundException");
		} catch (IOException e) {
			System.out.println("IOFehler");
			e.printStackTrace();
		} finally {

			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (client != null) {
				try {
					client.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (ss != null) {
				try {
					ss.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

}
