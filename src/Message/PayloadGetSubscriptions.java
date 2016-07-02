package Message;

import java.io.Serializable;

public class PayloadGetSubscriptions implements Payload, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final String[] subscriptions;

	public PayloadGetSubscriptions(String[] subscriptions) {
		this.subscriptions = subscriptions;
	}

	/**
	 * @return the producers
	 */
	public String[] getSubscriptions() {
		return subscriptions;
	}
}
