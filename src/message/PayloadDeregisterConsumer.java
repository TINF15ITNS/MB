package message;

import java.io.Serializable;

public class PayloadDeregisterConsumer implements Payload, Serializable {
	private boolean success = false;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Bei der Antwort vom Server soll die Id 0 sein, da nicht mehr existent.
	 * L�uft beim Server iwas schief, sprich er kann den konsument nicht
	 * abmelden, dann sendet er als Antwort die bisher g�ltige ID zur�ck
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