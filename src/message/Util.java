/**
 * 
 */
package message;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author Nikolai Seip, Sebastian Mattheis, Fabian Hinz
 *
 */
public class Util {
	
	/**
	 * wraps the message to a DatagramPacket
	 * 
	 * @param iadr
	 *            the InetAddress of the recipient
	 * @param port
	 *            port of the recipient
	 * @return a DatagramPacket
	 */
	public static DatagramPacket getMessageAsDatagrammPacket(Message m, InetAddress iadr, int port) {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		ObjectOutputStream objOut = null;
		try {
			objOut = new ObjectOutputStream(bout);
			objOut.writeObject(m);
			objOut.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] buf = bout.toByteArray();

		DatagramPacket dp = new DatagramPacket(buf, buf.length, iadr, port);
		dp.setData(buf);
		return dp;
	}

	/**
	 * wraps the data out of the DatagramPacket to a Message-Object
	 * 
	 * @param dp
	 *            the DatagramPacket
	 * @return message-object
	 */
	public static Message getMessageOutOfDatagramPacket(DatagramPacket dp) {
		byte[] buf = dp.getData();
		ByteArrayInputStream bin = new ByteArrayInputStream(buf); // von Datagram
		ObjectInputStream objIn = null;
		Message m = null;
		try {
			objIn = new ObjectInputStream(bin);
			m = (Message) objIn.readObject();
		} catch (ClassNotFoundException e) {
			System.out.println("Beim Lesen des Objektes ist ein fehler aufgetreten");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IOFehler beim ermitteln der Message ausm DatagramPacket");
			e.printStackTrace();
		} finally {
			if (objIn != null)
				try {
					objIn.close();
				} catch (IOException e) {
					System.out.println("Fehler beim closen vonm ObjectInputStream");
					e.printStackTrace();
				}
		}
		return m;
	}

	/**
	 * A method that opens a TCP connection with a server, sends a message, waits for an answer and closes the connection
	 * 
	 * @param message
	 *            The message that is supposed to be sent
	 * @param address
	 *            The address of the target server
	 * @param serverPort
	 *            The open port of the target server
	 * @return The Message response of the server
	 */
	public static Message sendAndGetMessage(Message message, InetAddress address, int serverPort) {

		try (Socket server = new Socket(address, serverPort);
				ObjectOutputStream out = new ObjectOutputStream(server.getOutputStream());
				ObjectInputStream in = new ObjectInputStream(server.getInputStream());) {

			out.writeObject(message);
			return (Message) in.readObject();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Checks if it is possible to establish a TCP connection to a server with specified address and port
	 * 
	 * @param address
	 *            The address of the target server
	 * @param serverPort
	 *            The port to be checked
	 * @param timeout
	 *            Timeout of the connection
	 * @return A boolean indicating success or failure
	 */
	public static boolean testConnection(InetAddress adress, int serverPort, int timeout) {

		try (Socket server = new Socket();) {
			server.connect(new InetSocketAddress(adress, serverPort), timeout);
			return true;
		} catch (IOException e) {
			return false;
		}
	}
}
