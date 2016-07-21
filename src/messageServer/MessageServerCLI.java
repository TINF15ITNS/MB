package messageServer;

public class MessageServerCLI {
	
	public static void main(String[] args) {
		System.out.println("Willkommen zum Message Broker - Server Interface\n================================================\n");
		MessageServerIF ms = new MessageServer(55555);
		System.out.println("Server auf Port " + ms.getServerPort() + " gestartet.");
		ms.respondOnMessages();

	}

}
