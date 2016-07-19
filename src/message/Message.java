package message;

import java.io.*;

public class Message implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * payload of this message
	 */
	/*
	 * private final String payload;
	 */
	private final Payload payload;
	/*
	 * 
	 * id of the Producer or Consumer who sent this message
	 * 
	 * private final int senderID;
	 */
	/**
	 * type of the message to indicate how to handle with this message
	 */
	private final MessageType type;

	/*
	 * public Message(MessageType type, int senderID, String payload) { this.type = type; this.senderID = senderID; this.payload = payload; }
	 */
	public Message(MessageType type, Payload payload) {
		this.type = type;
		this.payload = payload;
	}

	/**
	 * @return the payload
	 */
	public Payload getPayload() {
		return payload;
	}

	/**
	 * @return the type
	 */
	public MessageType getType() {
		return type;
	}

}
