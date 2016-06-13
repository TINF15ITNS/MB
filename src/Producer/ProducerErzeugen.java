package Producer;

import java.util.HashMap;

public class ProducerErzeugen {
	/**
	 * Variable wird durch den User über einen Button auf true gesetzt, wenn ein neuer Producer erzeugt werden soll.
	 */
	private boolean erzeugeProducer = false;
	private HashMap<Key, Producer> mengeProducer;

	// nachher Sichtbarkeit private, da nur von der eigenen main-Methode einmal ein Pbjekt der Klasse erzeugt werden soll
	public ProducerErzeugen() {
		mengeProducer = new HashMap<Key, Producer>();
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
		Key key = new Key(new Integer(Key.anzahl));
		Producer prod = new Producer(key);

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

class Key {
	/**
	 * Repräsentiert die Menge aller erzeugten Key-Objekte
	 */
	public static int anzahl = 0;
	/**
	 * Das Key-Attribut. Ist final deklariert, da sich der Schlüssel nicht ändern soll. Logischerweise existiert auch keine set-Methode
	 */
	private final Integer key;

	public Key(Integer i) {
		key = i;
		anzahl++;
	}

	public Integer getKey() {
		return key;
	}
}
