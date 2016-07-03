package message;

public class PayloadRegisterProducer implements Payload {
	
	private final String name;
	
	public PayloadRegisterProducer(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}

}
