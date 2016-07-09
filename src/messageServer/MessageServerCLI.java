package MessageServer;

import java.util.Scanner;

public class MessageServerCLI {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		System.out.println("Willkommen beim Installationsvorgang Ihres MessageServers:");
		System.out.print("Geben Sie bitte den Port an, über welchen Konsumenten und Produzenten den MessageServer erreichen können ");
		int portMessageServer = scanner.nextInt();

		MessageServer ms = new MessageServer(portMessageServer);

		System.out.println("\n\n\nIhr MessageServer wurde erfolgreich installiert\nSie erreichen den MessageServer unter folgendem Port: " + portMessageServer);

	}

}
