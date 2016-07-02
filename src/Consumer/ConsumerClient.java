package Consumer;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class ConsumerClient {
	private static int serverPort = 55555; //The port of the MessageServer

	public static void main(String[] args) {
		boolean correctInetAddress = false;
		InetAddress address = null;
		Scanner scanner = new Scanner(System.in);
		
		//Ask for a new address until a reachable one is provided
		while (!correctInetAddress) {
			System.out.print("Geben Sie bitte die Adresse des Servers (an ohne Port): ");
			try {
				address = InetAddress.getByName(scanner.nextLine()); //Parse the given string into a InetAddress object
				correctInetAddress = testConnection(address); //Test if there is someone listening via TCP
				
			} catch (UnknownHostException e) {
				System.out.println(e.getMessage());
				continue;
			}
		}
		scanner.close();
		
		Consumer user = new Consumer(address, serverPort);
		user.registerOnServer();
		user.registerOnMulticastGroup();
		user.startAction();
		

	}

	/**
	 * Checks if it is possible to establish a TCP connection using the "serverPort"
	 * @param adress The address of the server to be checked
	 * @return if the connection was successful 
	 */
	private static boolean testConnection(InetAddress adress) {
		Socket server = new Socket();
		try {
			server.connect(new InetSocketAddress(adress, serverPort), 1000);
			server.close();
			return true;
		} catch (IOException e) {
			return false;
		}

	}
}
