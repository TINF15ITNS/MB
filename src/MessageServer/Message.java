package MessageServer;

;

public class Message {
	private static final long serialVersionUID = 1L;
	private final String payload;
	private final MessageType type;

	public Message(MessageType type, String payload) {
		this.type = type;
		this.payload = payload;
	}

	/**
	 * @return the payload
	 */
	public String getPayload() {
		return payload;
	}

	/**
	 * @param payload
	 *            the payload to set
	 */

	/**
	 * @return the type
	 */
	public MessageType getType() {
		return type;
	}

	// Hier ist das wohl an der falschen Stelle
	// und auch scheiﬂe gemacht
	/*
	 * public File serialize() {
	 * 
	 * File file = new File("MessageXML"); try (XMLEncoder enc = new XMLEncoder(new FileOutputStream(file));) { enc.writeObject(this); } catch
	 * (FileNotFoundException e) { return null; } return file; }
	 */

	/*
	 * public Message deserialize() { File file = new File("MessageXML"); Message m; try (XMLDecoder enc = new XMLDecoder(new FileInputStream(file));) { m =
	 * (Message) enc.readObject(); } catch (FileNotFoundException e) { return null; } return m; }
	 */
}
