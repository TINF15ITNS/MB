package MessageServer;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class Message {
	private static final long serialVersionUID = 1L;
	private String payload;
	private MessageType type;

	public Message(MessageType type, String payload) {
		this.type = type;
		this.payload = payload;
	}

	public File serialize() {
		/*
		 * String rueckgabe = type + ":" + payload; return rueckgabe.getBytes();
		 */
		File file = new File("MessageXML");
		try (XMLEncoder enc = new XMLEncoder(new FileOutputStream(file));) {
			enc.writeObject(this);
		} catch (FileNotFoundException e) {
			return null;
		}
		return file;
	}

	public Message deserialize() {
		File file = new File("MessageXML");
		Message m;
		try (XMLDecoder enc = new XMLDecoder(new FileInputStream(file));) {
			m = (Message) enc.readObject();
		} catch (FileNotFoundException e) {
			return null;
		}
		return m;
	}
}
