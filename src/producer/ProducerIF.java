package producer;

public interface ProducerIF {
	/**
	 * sends a message to the server with the producer name, to be registered
	 * 
	 * @return a boolean indicating success or failure of the method
	 */
	public boolean registerOnServer();

	/**
	 * Sends a message to server asking to be removed from the list of registered producers
	 * 
	 * @return a boolean indicating success or failure of the method
	 */
	public boolean deregisterFromServer();

	/**
	 * Advice the producer to send a message to the given Server
	 * 
	 * @param msg the message to be sent
	 * @return a boolean indicating success or failure of the method
	 */
	public boolean sendMessage(String msg);
	
	/**
	 * 
	 * @return true if the producer is registered, false otherwise
	 */
	public boolean isRegistered();

}
