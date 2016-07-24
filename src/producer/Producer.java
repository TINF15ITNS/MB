/**
 * 
 */
package producer;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashSet;

import message.Message;
import message.MessageFactory;
import message.MessageType;
import message.PayloadBroadcast;
import message.PayloadProducer;
import message.PayloadProducerList;
import message.Util;

/**
 * 
 * @author Nikolai Seip, Sebastian Mattheis, Fabian Hinz
 *
 */
public class Producer implements ProducerIF {
	private static int serverPort = 55555;
	private String name;
	private boolean registered = false;
	private InetAddress serverAddress;

	public Producer(String name, String address) throws IllegalArgumentException, IOException {
		this.serverAddress = InetAddress.getByName(address.trim());
		if (!Util.testConnection(serverAddress, serverPort, 1000))
			throw new IOException("There is no server at the given address");
		HashSet<String> producers = getProducers();
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

	@Override
	public boolean registerOnServer() {
		Message answer;
		try {
			answer = Util.sendAndGetMessage(MessageFactory.createRegisterProducerMsg(name), serverAddress, serverPort);
		} catch (IOException e) {
			return false;
		}
		if (answer.getType() != MessageType.RegisterProducer || answer.getPayload() instanceof PayloadProducer) {
			throw new RuntimeException("Wrong Payload or MessageType");
		}
		PayloadProducer answerPayload = (PayloadProducer) answer.getPayload();

		if (answerPayload.getSuccess() == true) {
			registered = true;
			return true;
		} else
			return false;
	}

	@Override
	public boolean deregisterFromServer() {
		Message answer;
		try {
			answer = Util.sendAndGetMessage(MessageFactory.createDeregisterProducerMsg(name), serverAddress, serverPort);
		} catch (IOException e) {
			return false;
		}
		if (answer.getType() != MessageType.DeregisterProducer || answer.getPayload() instanceof PayloadProducer) {
			throw new RuntimeException("Wrong Payload or MessageType");
		}
		PayloadProducer answerPayload = (PayloadProducer) answer.getPayload();

		if (answerPayload.getSuccess() == true) {
			registered = false;
			return true;
		} else
			return false;
	}

	@Override
	public boolean sendMessage(String msg) {
		Message answer;
		try {
			answer = Util.sendAndGetMessage(MessageFactory.createBroadcastMessage(name, msg), serverAddress, serverPort);
		} catch (IOException e) {
			return false;
		}
		if (answer.getType() != MessageType.Broadcast || answer.getPayload() instanceof PayloadBroadcast) {
			throw new RuntimeException("Wrong Payload or MessageType");
		}
		PayloadBroadcast pm = (PayloadBroadcast) answer.getPayload();

		if (!pm.getSuccess()) {
			return false;
		}
		return true;
	}

	@Override
	public boolean isRegistered() {
		return registered;
	}

	private HashSet<String> getProducers() {
		Message answer;
		try {
			answer = Util.sendAndGetMessage(MessageFactory.createProducerListMsg(), serverAddress, serverPort);
		} catch (IOException e) {
			return null;
		}
		if (answer.getType() != MessageType.getProducerList || answer.getPayload() instanceof PayloadProducerList) {
			throw new RuntimeException("Wrong Payload or MessageType");
		}
		return ((PayloadProducerList) answer.getPayload()).getProducers();
	}
}