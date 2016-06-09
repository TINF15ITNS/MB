package MServer;

public class Message {
	private String payload;
	private MessageType type;

	public Message(MessageType type, String payload) {
		this.type = type;
		this.payload = payload;
	}

	public byte[] serialize() {
		String rueckgabe = type + ":" + payload;
		return rueckgabe.getBytes();

	}
}
