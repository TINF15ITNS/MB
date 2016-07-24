/**
 * 
 */
package message;

import java.io.Serializable;
import java.util.HashSet;

/**
 * 
 * @author Nikolai Seip, Sebastian Mattheis, Fabian Hinz
 *
 */
public class PayloadProducerList implements Payload, Serializable {
	
	private static final long serialVersionUID = 1L;

	private boolean success = false;
	private final HashSet<String> producers;

	public PayloadProducerList(HashSet<String> producers) {
		this.producers = producers;
	}
	
	public PayloadProducerList(HashSet<String> producers, boolean success) {
		this.producers = producers;
		this.success = success;
	}
	
	public PayloadProducerList(boolean success) {
		this.success = success;
		this.producers = null;
	}

	/**
	 * @return the producers
	 */
	public HashSet<String> getProducers() {
		return producers;
	}

	@Override
	public boolean getSuccess() {
		return this.success;
	}
}