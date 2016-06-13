package Producer;

import java.util.HashMap;

public class ProducerErzeugen {
	/*
	 * Variable wird durch den User über einen Button auf true gesetzt, wenn ein neuer Producer erzeugt werden soll. private boolean erzeugeProducer = false;
	 */
	private HashMap<Producer.Key, Producer> mengeProducer;

	// nachher Sichtbarkeit private, da nur von der eigenen main-Methode einmal ein Pbjekt der Klasse erzeugt werden soll
	public ProducerErzeugen() {
		mengeProducer = new HashMap<Producer.Key, Producer>();
	}

	public static void main(String[] args) {
		ProducerErzeugen prod = new ProducerErzeugen();
		GUIProducerErzeugen GUIPE = new GUIProducerErzeugen(prod);
		System.out.println("Betrete jetzt die While-Schleife");
		/*
		 * while (true) { if (prod.isErzeugeProducer()) { prod.erzeugeNeuenProducer(); } }
		 */

	}

	void erzeugeNeuenProducer(String name) {
		Producer prod = new Producer(name);

		// ?????????????????????????????????
		// richtiger Stil so ???????
		mengeProducer.put(prod.key, prod);
	}
}
