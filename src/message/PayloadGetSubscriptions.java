package message;

import java.io.Serializable;

public class PayloadGetSubscriptions implements Payload, Serializable {

	private boolean success = false;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final String[] subscriptions;

	public PayloadGetSubscriptions(String[] subscriptions) {
		this.subscriptions = subscriptions;
	}

	public PayloadGetSubscriptions(String[] subscriptions, boolean success) {
		this.subscriptions = subscriptions;
		this.success = success;
	}
	public PayloadGetSubscriptions(boolean success) {
		this.subscriptions = null;
		this.success = success;
	}
	/**
	 * @return the producers
	 */
	public String[] getSubscriptions() {
		return subscriptions;
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
