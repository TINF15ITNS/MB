package message;

import java.io.Serializable;

public class PayloadProducer implements Payload, Serializable {

	private final String name;
	private boolean success = false;

	public PayloadProducer(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public boolean getSuccess() {
		return success;
	}

	public void setSuccess() {
		success = true;
	}

}
