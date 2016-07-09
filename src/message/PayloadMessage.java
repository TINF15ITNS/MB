package message;

import java.io.Serializable;

public class PayloadMessage implements Payload, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final String name;

	private final String text;

	public PayloadMessage(String name, String text) {
		this.name = name;
		this.text = text;
	}

	/**
	 * @return the senderID
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}
}
