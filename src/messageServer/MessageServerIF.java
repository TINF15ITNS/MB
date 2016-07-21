package messageServer;

public interface MessageServerIF {
	/**
	 * waits for Messages from Producers or Consumers and responses them
	 */
	public void respondOnMessages();
	
	public int getServerPort();
}
