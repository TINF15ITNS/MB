package message;

import java.io.Serializable;

public class PayloadGetProducerList implements Payload, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private final String[] producers;

	public PayloadGetProducerList(String[] producers) {
		this.producers = producers;
	}

	/**
	 * @return the producers
	 */
	public String[] getProducers() {
		return producers;
	}

}