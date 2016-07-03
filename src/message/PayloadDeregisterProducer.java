package message;


public class PayloadDeregisterProducer implements Payload {
	
	private final String name;
	
	public PayloadDeregisterProducer(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}

}
