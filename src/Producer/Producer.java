package Producer;

public class Producer {
	Key key;
	GUIProducer gui;
	String name;

	public Producer(String n) {
		System.out.println("Bin im Konstruktor von Producer");
		this.key = new Key();
		this.name = n;
		gui = new GUIProducer(name);
	}

	static class Key {
		/**
		 * Repräsentiert die Menge aller erzeugten Key-Objekte
		 */
		public static int anzahl = 0;
		/**
		 * Das Key-Attribut. Ist final deklariert, da sich der Schlüssel nicht ändern soll. Logischerweise existiert auch keine set-Methode
		 */
		private final Integer key;

		public Key() {
			anzahl++;
			key = new Integer(anzahl);
		}

		public Integer getKey() {
			return key;
		}
	}
}
