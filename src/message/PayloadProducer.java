package message;

import java.io.Serializable;

public class PayloadProducer implements Payload, Serializable {

	private final String name;
	private boolean success = false;

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
	
	public String getName() {
		return this.name;
	}
	
	@Override
	public boolean getSuccess() {
		return success;
	}

	@Override
	public void setSuccess(boolean success) {
		this.success = success;
	}

}
