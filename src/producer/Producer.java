package producer;

import java.io.*;
import java.net.*;

import message.*;

public class Producer {
	private static int serverPort = 55555;
	private String name;
	private InetAddress serverAddress;

	public Producer(String name, String address) throws Exception, IOException {
		this.serverAddress = InetAddress.getByName(address);
		if (!Util.testConnection(serverAddress, serverPort, 1000))
			throw new IOException("There is no server at the given address");
		String[] producers = getProducers();
		for (String n : producers) {
			if (n.equalsIgnoreCase(name)) throw new Exception("This producer name is already taken");
		}
		this.name = name;
	}
	
	public boolean registerOnServer() {
		Message answer = Util.sendAndGetMessage(MessageFactory.createRegisterProducerMsg(name), serverAddress, serverPort);
		PayloadProducer answerPayload = (PayloadProducer) answer.getPayload();
		return answerPayload.getSuccess();
	}

	public boolean deregisterFromServer() {

		Message answer = Util.sendAndGetMessage(MessageFactory.createDeregisterProducerMsg(name), serverAddress, serverPort);
		PayloadProducer answerPayload = (PayloadProducer) answer.getPayload();
		return answerPayload.getSuccess();

	}
	
	//TODO: Implement Confirmation process
	public boolean broadcastMessage(String m) {
		Message answer = Util.sendAndGetMessage(new Message(MessageType.Message, new PayloadMessage(name, m)), serverAddress, serverPort);
		return true;
	}

	public String[] getProducers() {
		Message answer = Util.sendAndGetMessage(MessageFactory.createRequestProducerListMsg(), serverAddress, serverPort);
		return ((PayloadGetProducerList) answer.getPayload()).getProducers();
	}
}