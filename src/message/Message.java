package message;

import java.io.*;
import java.net.*;

public class Message implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * payload of this message
	 */
	/*
	 * private final String payload;
	 */
	private final Payload payload;
	/*
	 * 
	 * id of the Producer or Consumer who sent this message
	 * 
	 * private final int senderID;
	 */
	/**
	 * type of the message to indicate how to handle with this message
	 */
	private final MessageType type;

	/*
	 * public Message(MessageType type, int senderID, String payload) { this.type = type; this.senderID = senderID; this.payload = payload; }
	 */
	public Message(MessageType type, Payload payload) {
		this.type = type;
		this.payload = payload;
	}

	// sollte man dann an dieser Stelle auch ne Methode implementieren, die f�r das versenden von Messages sorgt, wie die MEthode sendAndGetMessages() in
	// Consumer? Rein aus sch�nheitsgr�nden, da hier auch die Methode getMessageAsDatagramPacket liegt?

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
	public static Message getMessageFromDatagramPacket(DatagramPacket dp) {
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
	 * @return the payload
	 */
	public Payload getPayload() {
		return payload;
	}

	/**
	 * @return the type
	 */
	public MessageType getType() {
		return type;
	}

}
