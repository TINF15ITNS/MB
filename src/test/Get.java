package test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import message.Message;
import message.PayloadBroadcast;
import message.Util;

public class Get {

	public static void main(String[] args) {
		MulticastSocket udps;
		int serverPort = 55555;
		try {
			InetAddress mcastadr = InetAddress.getByName("225.225.225.225");

			udps = new MulticastSocket(serverPort);
			udps.joinGroup(mcastadr);

			InetAddress serverAddress = InetAddress.getByName("localhost");

			byte[] buffer = new byte[65508];
			DatagramPacket dp = new DatagramPacket(buffer, buffer.length, serverAddress, serverPort);
			System.out.println(dp.getData().length);
			try {
				System.out.println("Warte jetzt auf Nachricht: ");
				udps.receive(dp);
				System.out.println("Hab ne UDP Nachricht erhalten und wandele sie jetzt in ne Message um");
				Message m = Util.getMessageOutOfDatagramPacket(dp);
				PayloadBroadcast pb = (PayloadBroadcast) m.getPayload();
				System.out.println(pb.getSender() + " meldet: " + pb.getMessage());

			} catch (IOException e) {
				System.out.println("Fehler beim Bearbeiten der erhaltenen UDP-Message");
				e.printStackTrace();
			}

			udps.leaveGroup(mcastadr);
			udps.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
