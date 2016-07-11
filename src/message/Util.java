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
	
	/**
	 * A method that opens a TCP connection with a server, sends a message,
	 * waits for an answer and closes the connection
	 * 
	 * @param message
	 * The message that is supposed to be sent
	 * @param address
	 * The address of the target server
	 * @param serverPort
	 * The open port of the target server
	 * @return
	 * The Message response of the server
	 */
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
	 * Checks if it is possible to establish a TCP connection to a server
	 * with specified address and port
	 * 
	 * @param address
	 * The address of the target server
	 * @param serverPort
	 * The port to be checked
	 * @param timeout
	 * Timeout of the connection
	 * @return 
	 * A boolean indicating success or failure
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
