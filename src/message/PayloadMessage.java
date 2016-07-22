package message;

import java.io.Serializable;

public class PayloadMessage implements Payload, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String sender;
	private final String message;
	private boolean success = false;
	
	
	public PayloadMessage(String sender, String message) {
		this.sender = sender;
		this.message = message;
	}
	
	public PayloadMessage(String sender, String message, boolean success) {
		this.sender = sender;
		this.message = message;
		this.success = success;
	}
	
	public PayloadMessage(boolean success) {
		this.sender = null;
		this.message = null;
		this.success = success;
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

	@Override
	public boolean getSuccess() {
		return this.success;
	}

	@Override
	public void setSuccess(boolean success) {
		this.success = success;
	}
}
