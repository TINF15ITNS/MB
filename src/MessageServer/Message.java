package MessageServer;

import java.net.DatagramPacket;
import java.net.InetAddress;

public class Message {
	/**
	 * payload of this message
	 */
	private final String payload;
	/**
	 * id of the Producer or Consumer who sent this message
	 */
	private final int consignorID;
	/**
	 * type of the message to indicate how to handle with this message
	 */
	private final MessageType type;

	public Message(MessageType type, int consignorID, String payload) {
		this.type = type;
		this.consignorID = consignorID;
		this.payload = payload;
	}

	/**
	 * wraps the message to a DatagramPacket
	 * 
	 * @param iadr
	 *            the InetAddress of the recipient
	 * @param port
	 *            port of the recipient
	 * @return a DatagramPacket
	 */
	public DatagramPacket getMessageAsDatagrammPacket(InetAddress iadr, int port) {
		String message = this.toString();
		int length = message.getBytes().length;
		DatagramPacket dp = new DatagramPacket(new byte[length], length, iadr, port);
		dp.setData(message.getBytes());
		return dp;
	}

	/**
	 * @return the payload
	 */
	public String getPayload() {
		return payload;
	}

	/**
	 * @return the type
	 */
	public MessageType getType() {
		return type;
	}

	/**
	 * @return the consignorID
	 */
	public int getConsignorID() {
		return consignorID;
	}

	@Override
	public String toString() {
		return new String(type + ";" + consignorID + ";" + payload);

	}

}
