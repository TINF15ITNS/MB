/**
 * 
 */
package messageServer;

/**
 * 
 * @author Nikolai Seip, Sebastian Mattheis, Fabian Hinz
 *
 */
public interface MessageServerIF {
	/**
	 * waits for Messages from Producers or Consumers and responses them
	 */
	public void respondOnMessages();

	/**
	 * 
	 * @return the port this server is running on
	 */
	public int getServerPort();
}
