package Message;

/**
 * 
 * @author Nikolai Seip, Sebastian Mattheis, Fabian Hinz
 *
 */
public enum MessageType {
	RegisterConsumer,
	DeregisterConsumer,
	SubscribeProducers,
	UnsubscribeProducers,
	getProducerList,
	getSubscriptions,
	Message,
	RegisterProducer,
	DeregisterProducer,
}
