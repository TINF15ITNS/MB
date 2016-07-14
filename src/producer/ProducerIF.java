package producer;

public interface ProducerIF {
	/**
	 * registers the producer on the server and checks if this producer name exits yet or not
	 * 
	 * @return if the operation was successful
	 */
	public boolean registerOnServer();

	/**
	 * deregisters the producer on the server
	 * 
	 * @return if the operation was successful
	 */
	public boolean deregisterFromServer();

	/**
	 * sends a message to the Server
	 * 
	 * @param msg
	 *            messagetext
	 * @return if the operation was successful
	 */
	public boolean sendMessage(String msg);

}
