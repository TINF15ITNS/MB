package test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

import message.*;

public class Send {

	public static void main(String[] args) {
		InetAddress multicastadr;
		int serverPort = 55555;
		try (MulticastSocket udpSocket = new MulticastSocket(serverPort);) {
			multicastadr = InetAddress.getByName("225.225.225.225");
			InetAddress serverAddress = InetAddress.getByName("localhost");

			udpSocket.setTimeToLive(1);

			Message m = new Message(MessageType.Broadcast, new PayloadBroadcast("Test", "Hallo!!!"));
			// schauen, ob der Absender sich beim Server auch angemeldet hat
			DatagramPacket dp = Util.getMessageAsDatagrammPacket(m, multicastadr, serverPort);
			System.out.println(dp.getData().length);

			System.out.println("Sende jetzt die Nachricht");
			udpSocket.send(dp);
			System.out.println("Abgeschickt");

			System.out.println("\nTesten wir das umwandeln mal\n");
			Message a = Util.getMessageOutOfDatagramPacket(dp);
			PayloadBroadcast pb = (PayloadBroadcast) a.getPayload();
			System.out.println(pb.getSender() + " meldet: " + pb.getMessage());

		} catch (Exception e) {
			System.out.println("Exception: ");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
