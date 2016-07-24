/**
 * 
 */
package consumer;

import java.util.HashSet;

/**
 * 
 * @author Nikolai Seip, Sebastian Mattheis, Fabian Hinz
 *
 */
public interface ConsumerIF {

	/**
	 * Registers this consumer on the server and starts an new thread for receiving messages from the server
	 * 
	 * @return true if the operation was successful, false if not
	 */
	public boolean registerOnServer();

	/**
	 * Fetches all available producers
	 * 
	 * @return all available producers, an empty string array if there are none, null if the operation was unsuccessful
	 */
	public HashSet<String> getProducers();

	/**
	 * Subscribes this user to the given producers
	 * 
	 * @param producers
	 *            The names of the producers
	 * @return a list of producers, it wasn't possible to subscribe to
	 */
	public String[] subscribeToProducers(String[] producers);

	/**
	 * Fetches a list of all producers this consumer is subscribed to
	 * 
	 * @return an array of producers this consumer is subscribed to
	 */
	public String[] getSubscriptions();

	/**
	 * Unsubscribes this consumer from the given producers
	 * 
	 * @param producers
	 *            The names of the producers
	 * @return a list of producers, it was not possible to unsubscribe from
	 */
	public String[] unsubscribeFromProducers(String[] producers);

	/**
	 * Deregisters this consumer on the server
	 * 
	 * @return if the operation was successful
	 */
	public boolean deregisterFromServer();

	/**
	 * 
	 * @return if this consumer is registered on the server
	 */
	public boolean isRegistered();

	/**
	 * When the user wants to read his received broadcasts, he calls this method to get them
	 * 
	 * @return a String with all of the broadcasts
	 */
	public String getNewBroadcasts();

	/**
	 * Stops receiving new Broadcasts
	 * 
	 * @return true if the receiving has stopped, false if the receiving has already been stopped
	 */
	public boolean stopReceiving();
}
