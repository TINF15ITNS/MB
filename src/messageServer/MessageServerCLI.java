/**
 * 
 */
package messageServer;

/**
 * 
 * @author Nikolai Seip, Sebastian Mattheis, Fabian Hinz
 *
 */
public class MessageServerCLI {

	public static void main(String[] args) {
		System.out.println("Willkommen zum Message Broker - Server Interface\n" + "================================================\n");
		MessageServerIF messageServer = new MessageServer(55555);
		System.out.println("Server auf Port " + messageServer.getServerPort() + " gestartet.");
		messageServer.respondOnMessages();

	}

}
