package message;

import java.io.Serializable;

public class PayloadMessage implements Payload, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final int senderID;

	private final String text;

	public PayloadMessage(int senderID, String text) {
		this.senderID = senderID;
		this.text = text;
	}

	/**
	 * @return the senderID
	 */
	public int getSenderID() {
		return senderID;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}
}
