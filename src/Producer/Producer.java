package Producer;

public class Producer {
	Key key;

	public Producer() {
		this.key = new Key();
	}

	static class Key {
		/**
		 * Repr�sentiert die Menge aller erzeugten Key-Objekte
		 */
		public static int anzahl = 0;
		/**
		 * Das Key-Attribut. Ist final deklariert, da sich der Schl�ssel nicht �ndern soll. Logischerweise existiert auch keine set-Methode
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
