package MServer;

import java.util.HashMap;

public class MServer {

	HashMap<Integer, Object> hashMapProducer, hashMapConsumer;

	public static void main(String[] args) {
		MServer messageServer = new MServer();

	}

	public MServer() {
		hashMapProducer = new HashMap<Integer, Object>();
		hashMapConsumer = new HashMap<Integer, Object>();

	}

}
