package message;

import java.io.Serializable;

public class PayloadDeregisterConsumer implements Payload, Serializable {

	private static final long serialVersionUID = 1L;

	private boolean success = false;

	/**
	 * ID of the sender of this message
	 */
	private final int senderID;

	public PayloadDeregisterConsumer(int senderID) {
		this.senderID = senderID;
	}

	public PayloadDeregisterConsumer(int senderID, boolean success) {
		this.senderID = senderID;
		this.success = success;
	}

	public PayloadDeregisterConsumer(boolean success) {
		this.senderID = 0;
		this.success = success;
	}

	/**
	 * @return the senderID
	 */
	public int getSenderID() {
		return senderID;
	}

	@Override
	public boolean getSuccess() {
		return success;
	}

	@Override
	public void setSuccess(boolean success) {
		this.success = success;
	}

}