package Message;

import java.io.Serializable;

public class PayloadForMessageTypeMessage implements Payload, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final String consignorName;

	private final String text;

	public PayloadForMessageTypeMessage(String consignorName, String text) {
		this.consignorName = consignorName;
		this.text = text;
	}

	/**
	 * @return the consignorName
	 */
	public String getConsignorName() {
		return consignorName;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}
}