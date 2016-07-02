package Message;

/**
 * 
 * @author Nikolai Seip, Sebastian Mattheis, Fabian Hinz
 *
 */
public enum MessageType {
	RegisterConsumer,
	DeregisterConsumer,
	SubscribeProducers, //Implemented
	UnsubscribeProducers, //Implemented
	getProducerList, //Implemented
	getSubscriptions, //Implemented
	Message, //Implemented
	RegisterProducer,
	DeregisterProducer,
}
