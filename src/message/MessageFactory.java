/**
 * 
 */
package message;

import java.net.InetAddress;
import java.util.HashSet;

/**
 * Factory class used to produce messages
 * 
 * @author Nikolai Seip, Sebastian Mattheis, Fabian Hinz
 *
 */
public class MessageFactory {

	public static Message createRegisterConsumerMsg() {
		return new Message(MessageType.RegisterConsumer, null);
	}

	public static Message createRegisterConsumerMsg(int consumerID, InetAddress multicastAddress, boolean success) {
		return new Message(MessageType.RegisterConsumer, new PayloadRegisterConsumer(consumerID, multicastAddress, success));

	}

	public static Message createDeregisterConsumerMsg(int consumerID) {
		return new Message(MessageType.DeregisterConsumer, new PayloadDeregisterConsumer(consumerID));
	}

	public static Message createDeregisterConsumerMsg(boolean success) {
		return new Message(MessageType.DeregisterConsumer, new PayloadDeregisterConsumer(success));

	}

	public static Message createRegisterProducerMsg(String name) {
		if (name == null)
			throw new IllegalArgumentException("A Producer must have a name!");
		return new Message(MessageType.RegisterProducer, new PayloadProducer(name));
	}

	public static Message createRegisterProducerMsg(String name, boolean success) {
		if (name == null)
			throw new IllegalArgumentException("A Producer must have a name!");
		return new Message(MessageType.RegisterProducer, new PayloadProducer(name, success));
	}

	public static Message createDeregisterProducerMsg(String name) {
		if (name == null)
			throw new IllegalArgumentException("A Producer must have a name!");
		return new Message(MessageType.DeregisterProducer, new PayloadProducer(name));
	}

	public static Message createDeregisterProducerMsg(String name, boolean success) {
		return new Message(MessageType.DeregisterProducer, new PayloadProducer(name, success));

	}

	public static Message createProducerListMsg() {
		return new Message(MessageType.getProducerList, null);
	}

	public static Message createProducerListMsg(HashSet<String> producerList, boolean success) {
		if (producerList == null)
			throw new IllegalArgumentException("Please enter a valid List!");
		return new Message(MessageType.getProducerList, new PayloadProducerList(producerList, success));
	}

	public static Message createBroadcastMessage(String sender, String message) {
		if (sender == null)
			throw new IllegalArgumentException("You cannot send a message without a sender");
		if (message == null)
			throw new IllegalArgumentException("You cannot send a message without content");
		return new Message(MessageType.Broadcast, new PayloadBroadcast(sender, message));
	}

	public static Message createBroadcastMessage(String sender, boolean success) {
		if (sender == null)
			throw new IllegalArgumentException("You cannot send a message without a sender");
		return new Message(MessageType.Broadcast, new PayloadBroadcast(sender, null, success));
	}

}
