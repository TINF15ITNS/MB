/**
 * 
 */
package message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author Nikolai Seip, Sebastian Mattheis, Fabian Hinz
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
	
	/**
	 * Checks if it is possible to establish a TCP connection with a
	 * specified address and port
	 * 
	 * @param address
	 *            The address of the server to be checked
	 * @param serverPort
	 *            The port to be checked
	 * @param timeout
	 *            Timeout of the connection
	 * @return a boolean indicating success or failure
	 */
	public static boolean testConnection(InetAddress adress, int serverPort, int timeout) {
		Socket server = new Socket();
		try {
			server.connect(new InetSocketAddress(adress, serverPort), timeout);
			server.close();
			return true;
		} catch (IOException e) {
			return false;
		}

	}



}
