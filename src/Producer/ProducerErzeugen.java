package Producer;

import java.util.HashMap;

public class ProducerErzeugen {
	/**
	 * Variable wird durch den User über einen Button auf true gesetzt, wenn ein neuer Producer erzeugt werden soll.
	 */
	private boolean erzeugeProducer = false;
	private HashMap<Producer.Key, Producer> mengeProducer;

	// nachher Sichtbarkeit private, da nur von der eigenen main-Methode einmal ein Pbjekt der Klasse erzeugt werden soll
	public ProducerErzeugen() {
		mengeProducer = new HashMap<Producer.Key, Producer>();
	}

	public static void main(String[] args) {
		ProducerErzeugen prod = new ProducerErzeugen();
		GUIProducerErzeugen GUIPE = new GUIProducerErzeugen(prod);
		while (true) {
			if (prod.isErzeugeProducer()) {
				prod.erzeugeNeuenProducer();
			}
		}

	}

	private void erzeugeNeuenProducer() {
		// Schritt 1: Key-Objekt erzeugen
		// Dazu wird das Key-Attribut auf die Anzahl der bisher erzeugten Key-Objekte gesetzt
		Producer prod = new Producer();

		// ?????????????????????????????????
		// richtiger Stil so ???????
		mengeProducer.put(prod.key, prod);
	}

	public boolean isErzeugeProducer() {
		return erzeugeProducer;
	}

	public void setErzeugeProducer(boolean erzeugeProducer) {
		this.erzeugeProducer = erzeugeProducer;
	}

}
