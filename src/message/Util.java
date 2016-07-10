/**
 * 
 */
package message;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @author naibafo
 *
 */
public class Util {
	public static Message sendAndGetMessage(Message message, InetAddress address, int serverPort) {
		Socket server;
		try {
			server = new Socket(address, serverPort);

			Message answer = null;
			ObjectOutputStream out = null;
			ObjectInputStream in = null;

			out = new ObjectOutputStream(server.getOutputStream());
			in = new ObjectInputStream(server.getInputStream());
			out.writeObject(message);
			answer = (Message) in.readObject();

			in.close();
			out.close();
			server.close();
			return answer;
		} catch (Exception e) {
			return null;
		}
	}


}
