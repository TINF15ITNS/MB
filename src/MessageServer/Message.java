package MessageServer;

import java.net.DatagramPacket;
import java.net.InetAddress;

public class Message {
	private final String payload;
	private final int consignorID;
	private final MessageType type;

	public Message(MessageType type, int consignorID, String payload) {
		this.type = type;
		this.consignorID = consignorID;
		this.payload = payload;
	}

	public DatagramPacket getMessageAsDatagrammPacket(InetAddress iadr, int port) {
		String message = this.toString();
		DatagramPacket dp = new DatagramPacket(new byte[message.getBytes().length], message.getBytes().length, iadr, port);
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
