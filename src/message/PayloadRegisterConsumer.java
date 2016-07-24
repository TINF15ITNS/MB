/**
 * 
 */
package message;

import java.io.Serializable;
import java.net.InetAddress;

/**
 * 
 * @author Nikolai Seip, Sebastian Mattheis, Fabian Hinz
 * 
 */

public class PayloadRegisterConsumer extends Payload implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * The consumer requests an id from the server. At the request-message the ID-attribut of consumer hasn't been set yet. But with the response-Message the
	 * server informs the consumer about his ID.
	 */
	private final int id;

	/**
	 * Same as attribute id
	 */
	private final InetAddress multicastAddress;

	public PayloadRegisterConsumer(int id, InetAddress multi, boolean success) {
		this.id = id;
		this.multicastAddress = multi;
		this.success = success;
	}

	public PayloadRegisterConsumer(boolean success) {
		this.id = 0;
		this.multicastAddress = null;
		this.success = success;
	}

	/**
	 * @return the id. This is the information carried by this message.
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the multicastAddress
	 */
	public InetAddress getMulticastAddress() {
		return multicastAddress;
	}
}
