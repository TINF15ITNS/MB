/**
 * 
 */
package message;

import java.io.Serializable;

/**
 * 
 * @author Nikolai Seip, Sebastian Mattheis, Fabian Hinz
 *
 */
public class PayloadBroadcast extends Payload implements Serializable {

	private static final long serialVersionUID = 1L;

	private final String sender;
	private final String message;

	public PayloadBroadcast(String sender, String message) {
		this.sender = sender;
		this.message = message;
	}

	public PayloadBroadcast(String sender, String message, boolean success) {
		this.sender = sender;
		this.message = message;
		this.success = success;
	}

	public PayloadBroadcast(boolean success) {
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

}
