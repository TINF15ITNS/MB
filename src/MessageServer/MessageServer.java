package MessageServer;

import java.util.HashMap;

public class MessageServer {

	HashMap<Integer, Object> hashMapProducer, hashMapConsumer;

	public static void main(String[] args) {
		MessageServer messageServer = new MessageServer();

	}

	public MessageServer() {
		hashMapProducer = new HashMap<Integer, Object>();
		hashMapConsumer = new HashMap<Integer, Object>();

	}

}
