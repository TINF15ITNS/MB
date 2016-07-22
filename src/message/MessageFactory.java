/**
 * 
 */
package message;

/**
 * @author Nikolai Seip, Sebastian Mattheis, Fabian Hinz
 *
 */
public class MessageFactory {
	
	public static Message createRegisterProducerMsg(String name) {
		if (name == null) throw new IllegalArgumentException("A Producer must have a name!");
		return new Message(MessageType.RegisterProducer, new PayloadProducer(name));
	}
	
	public static Message createDeregisterProducerMsg(String name) {
		if (name == null) throw new IllegalArgumentException("A Producer must have a name!");
		return new Message(MessageType.DeregisterProducer, new PayloadProducer(name));
	}
	
	public static Message createRequestProducerListMsg() {
		return new Message(MessageType.getProducerList, null);
	}
	
	public static Message createBroadcastMessage(String sender, String message) {
		if (sender == null) throw new IllegalArgumentException("You cannot send a message without a sender");
		if (message == null) throw new IllegalArgumentException("You cannot send a message without content");
		return new Message(MessageType.Broadcast, new PayloadBroadcast(sender, message));
	}


}
