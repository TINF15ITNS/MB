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
public class PayloadProducer extends Payload implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * name of the producer, who wants to register or deregister
	 */
	private final String name;

	public PayloadProducer(String name) {
		this.name = name;
	}

	public PayloadProducer(String name, boolean success) {
		this.name = name;
		this.success = success;
	}

	public PayloadProducer(boolean success) {
		this.name = null;
		this.success = success;
	}

	/**
	 * 
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}
}
