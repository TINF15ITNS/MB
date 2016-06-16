package MessageServer;

import java.util.HashMap;

import Producer.Producer;

public class MessageServer {

	HashMap<Integer, Object> hashMapProducer, hashMapConsumer;

	public static void main(String[] args) {
		MessageServer messageServer = new MessageServer();

	}

	public MessageServer() {
		HashMap<Key, Producer> mengeProducer;
		hashMapConsumer = new HashMap<Integer, Object>();

	}

	public void register(Producer prod) {
		prod.setKey(new Key());
	}

}
