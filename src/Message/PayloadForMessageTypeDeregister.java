package Message;

import java.io.Serializable;

public class PayloadForMessageTypeDeregister implements Payload, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Bei der Antwort vom Server soll die Id 0 sein, da nicht mehr existent. Läuft beim Server iwas schief, sprich er kann den konsument nicht abmelden, dann
	 * sendet er als Antwort die bisher gültige ID zurück
	 */
	private final int consignorID;

	public PayloadForMessageTypeDeregister(int consignorID) {
		this.consignorID = consignorID;
	}

	/**
	 * @return the consignorID
	 */
	public int getConsignorID() {
		return consignorID;
	}
}