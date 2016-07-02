package Message.Deprecated;

import java.io.Serializable;

import Message.Payload;
@Deprecated
public class PayloadRegisterOnProducer implements Payload, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final int consignorID;

	private final String[] producers;
	// entweder im Format "<producerID>:<producerName>" oder nur "<producerName>" wenn dieser eindeutig sein soll

	public PayloadRegisterOnProducer(int consignorID, String[] producers) {
		this.consignorID = consignorID;
		this.producers = producers;
	}

	/**
	 * @return the producers
	 */
	public String[] getProducers() {
		return producers;
	}

	/**
	 * @return the consignorID
	 */
	public int getConsignorID() {
		return consignorID;
	}
}
