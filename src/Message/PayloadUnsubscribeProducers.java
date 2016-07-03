package Message;

import java.io.Serializable;

public class PayloadUnsubscribeProducers implements Payload, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String[] toBeUnsubscribed;

	public PayloadUnsubscribeProducers(String[] toBeUnsubscribed) {
		this.toBeUnsubscribed = toBeUnsubscribed;
	}

	/**
	 * @return the producers
	 */
	public String[] getToBeUnsubscribed() {
		return toBeUnsubscribed;
	}
}
