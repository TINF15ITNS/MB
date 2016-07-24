/**
 * 
 */
package message;

/**
 * All available message types
 * 
 * @author Nikolai Seip, Sebastian Mattheis, Fabian Hinz
 *
 */
public enum MessageType {
	RegisterConsumer,
	DeregisterConsumer,
	getProducerList,
	Broadcast,
	RegisterProducer,
	DeregisterProducer
}
