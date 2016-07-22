package message;

import java.io.Serializable;
import java.util.HashSet;

public class PayloadGetProducerList implements Payload, Serializable {

	private boolean success = false;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final HashSet<String> producers;

	public PayloadGetProducerList(HashSet<String> producers) {
		this.producers = producers;
	}
	
	public PayloadGetProducerList(HashSet<String> producers, boolean success) {
		this.producers = producers;
		this.success = success;
	}
	
	public PayloadGetProducerList(boolean success) {
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

	@Override
	public void setSuccess(boolean success) {
		this.success = success;
	}

}