package Message;

import java.io.Serializable;

public class PayloadForMessageTypegetProducer implements Payload, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * the consumer wants subscribe on the producers in this stringarray. If this array in the answer of the server isnt null, the consumer wasn't be able to
	 * subscribe on the producers in the array
	 */
	private final String[] producers;

	public PayloadForMessageTypegetProducer(String[] producers) {
		this.producers = producers;
	}

	/**
	 * @return the producers
	 */
	public String[] getProducers() {
		return producers;
	}

}