package Producer;

import java.util.HashMap;

public class ProducerErzeugen {
	private HashMap<Producer.Key, Producer> mengeProducer;

	// nur von der eigenen main-Methode soll einmal ein Objekt der Klasse erzeugt werden
	private ProducerErzeugen() {
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
