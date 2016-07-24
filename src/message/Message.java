/**
 * 
 */
package message;

import java.io.Serializable;

/**
 * Container to be sent containing specific payloads
 * 
 * @author Nikolai Seip, Sebastian Mattheis, Fabian Hinz
 *
 */
public class Message implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * payload of this message
	 */
	private final Payload payload;
	/**
	 * type of the message to indicate how to handle with this message
	 */
	private final MessageType type;

	protected Message(MessageType type, Payload payload) {
		this.type = type;
		this.payload = payload;
	}

	/**
	 * @return the payload
	 */
	public Payload getPayload() {
		return payload;
	}

	/**
	 * @return the type
	 */
	public MessageType getType() {
		return type;
	}

}
