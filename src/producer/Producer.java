package producer;

import java.io.*;
import java.net.*;
import message.*;

/**
 * @author Nikolai Seip, Sebastian Mattheis, Fabian Hinz
 *
 */
public class Producer implements ProducerIF {
	private static int serverPort = 55555;
	private String name;
	private boolean registered = false;
	private InetAddress serverAddress;

	public Producer(String name, String address) throws IllegalArgumentException, IOException {
		this.serverAddress = InetAddress.getByName(address);
		if (!Util.testConnection(serverAddress, serverPort, 1000))
			throw new IOException("There is no server at the given address");
		String[] producers = getProducers();
		if (producers != null) {
			for (String n : producers) {
				if (n.equalsIgnoreCase(name))
					throw new IllegalArgumentException("This producer name is already taken");
			}
			this.name = name;
		} else {
			throw new IOException("No server reachable");
		}
	}

	public boolean registerOnServer() {
		Message answer;
		try {
			answer = Util.sendAndGetMessage(MessageFactory.createRegisterProducerMsg(name), serverAddress, serverPort);
		} catch (IOException e) {
			return false;
		}
		PayloadProducer answerPayload = (PayloadProducer) answer.getPayload();
		if (answerPayload.getSuccess() == true) {
			registered = true;
			return true;
		} else
			return false;
	}

	public boolean deregisterFromServer() {
		Message answer;
		try {
			answer = Util.sendAndGetMessage(MessageFactory.createDeregisterProducerMsg(name), serverAddress, serverPort);
		} catch (IOException e) {
			return false;
		}
		PayloadProducer answerPayload = (PayloadProducer) answer.getPayload();
		if (answerPayload.getSuccess() == true) {
			registered = false;
			return true;
		} else
			return false;
	}

	public boolean sendMessage(String msg) {
		Message answer;
		try {
			answer = Util.sendAndGetMessage(MessageFactory.createBroadcastMessage(name, msg), serverAddress, serverPort);
		} catch (IOException e) {
			return false;
		}
		PayloadMessage pm = (PayloadMessage) answer.getPayload();
		if (!pm.getSuccess()) {
			return false;
		}
		return true;
	}

	public boolean isRegistered() {
		return registered;
	}

	private String[] getProducers() {
		Message answer;
		try {
			answer = Util.sendAndGetMessage(MessageFactory.createRequestProducerListMsg(), serverAddress, serverPort);
		} catch (IOException e) {
			return null;
		}
		return ((PayloadGetProducerList) answer.getPayload()).getProducers();
	}
}