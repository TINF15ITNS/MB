package message;

import java.io.Serializable;

public class PayloadDeregisterConsumer implements Payload, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Bei der Antwort vom Server soll die Id 0 sein, da nicht mehr existent. L�uft beim Server iwas schief, sprich er kann den konsument nicht abmelden, dann
	 * sendet er als Antwort die bisher g�ltige ID zur�ck
	 */
	private final int consignorID;

	public PayloadDeregisterConsumer(int consignorID) {
		this.consignorID = consignorID;
	}

	/**
	 * @return the consignorID
	 */
	public int getConsignorID() {
		return consignorID;
	}
}