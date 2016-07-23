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
public class PayloadDeregisterConsumer implements Payload, Serializable {

	private static final long serialVersionUID = 1L;

	private boolean success = false;

	/**
	 * ID of the sender of this message
	 */
	private final int id;

	public PayloadDeregisterConsumer(int id) {
		this.id = id;
	}

	public PayloadDeregisterConsumer(int id, boolean success) {
		this.id = id;
		this.success = success;
	}

	public PayloadDeregisterConsumer(boolean success) {
		this.id = 0;
		this.success = success;
	}

	/**
	 * @return the ID
	 */
	public int getID() {
		return id;
	}

	@Override
	public boolean getSuccess() {
		return success;
	}
}