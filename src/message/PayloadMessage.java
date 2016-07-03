package message;

import java.io.Serializable;

public class PayloadMessage implements Payload, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final String senderName;

	private final String text;

	public PayloadMessage(String senderName, String text) {
		this.senderName = senderName;
		this.text = text;
	}

	/**
	 * @return the senderName
	 */
	public String getSenderName() {
		return senderName;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}
}