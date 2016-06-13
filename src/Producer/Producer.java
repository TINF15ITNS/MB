package Producer;

public class Producer {
	Key key;

	public Producer() {
		this.key = new Key();
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
