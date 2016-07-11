package message;

import java.io.Serializable;

public class PayloadMessage implements Payload, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final String sender;

	private final String message;

	public PayloadMessage(String sender, String message) {
		this.sender = sender;
		this.message = message;
	}

	/**
	 * @return the senderID
	 */
	public String getSender() {
		return sender;
	}

	/**
	 * @return the text
	 */
	public String getMessage() {
		return message;
	}
}
