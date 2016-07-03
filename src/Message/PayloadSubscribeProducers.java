package Message;

import java.io.Serializable;

public class PayloadSubscribeProducers implements Payload, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String[] toBeSubscribed;

	public PayloadSubscribeProducers(String[] toBeSubscribed) {
		this.toBeSubscribed = toBeSubscribed;
	}

	/**
	 * @return the producers
	 */
	public String[] getToBeSubscribed() {
		return toBeSubscribed;
	}
}
