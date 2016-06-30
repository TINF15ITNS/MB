package MessageServer;

import java.io.Serializable;
import java.net.InetAddress;

/**
 * 
 * a message of the type MessageType.registerOnServer contains as payload an object of this class
 * 
 * @author Nikolai
 */
public class PayloadForMessageTypeRegisterOnServer implements Payload, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The consumer requests an id from the server. At the request-message the ID-attribut of consumer hasn't been set yet. But with the response-Message the
	 * server informs the consumer about his ID.
	 */
	private final int id;

	/**
	 * Same as attribut id
	 */
	private final InetAddress multicastAddress;

	// der Server brauch den namen des Consumers ja gar nicht zu wissen ... kann dem egal sein, wenns ne eindeutige ID gibt

	public PayloadForMessageTypeRegisterOnServer(int id, InetAddress multi) {
		this.id = id;
		this.multicastAddress = multi;
	}

	/**
	 * @return the id. This is the information carried by this message.
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the multicastAddress
	 */
	public InetAddress getMulticastAddress() {
		return multicastAddress;
	}

}
