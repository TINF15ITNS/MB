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


}
