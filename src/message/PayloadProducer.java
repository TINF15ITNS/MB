package message;

public class PayloadProducer implements Payload {
	
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

}
