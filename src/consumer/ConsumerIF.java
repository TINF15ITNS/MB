package consumer;

public interface ConsumerIF {

	/**
	 * Registers the user on the server and starts this user receiving messages from the server
	 */
	public void registerOnServer();

	/**
	 * Fetches all available producers
	 * 
	 * @return all available producers
	 */
	public String[] getProducers();

	/**
	 * Subscribes this user to the given producers
	 * 
	 * @param producers
	 *            The names of the producers (can not be null)
	 * @return a list of producers, it wasn't possible to subscribe on
	 */
	public String[] subscribeToProducers(String[] producers);

	/**
	 * Fetches a list of all producers this consumer is subscribed to
	 * 
	 * @return a list of producers this consumer is subscribed to
	 */
	public String[] getSubscriptions();

	/**
	 * Unsubscribes this user from the given producers
	 * 
	 * @param producers
	 *            The names of the producers (can not be null)
	 * @return
	 */
	public String[] unsubscribeFromProducers(String[] producers);

	/**
	 * Registers the user on the server
	 * 
	 * @return if the operation was successful
	 */
	public boolean deregisterFromServer();
}
