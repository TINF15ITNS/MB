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
		 * Repräsentiert die Menge aller aktuell existierenden Key-Objekte. Relevant für die hashCode()-Methode
		 */
		static int aktuelleAnzahl = 0;
		/**
		 * Das Key-Attribut. Ist final deklariert, da sich der Schlüssel nicht ändern soll. Logischerweise existiert auch keine set-Methode
		 */
		private final Integer key;

		public Key() {
			anzahl++;
			aktuelleAnzahl++;

			key = new Integer(anzahl);
		}

		// protected muss sie mindestens sein
		protected void finalize() {
			// auch wenn die finalize()-Methode nicht garantiert direkt aufgerufen wird, nachdem das Objekt freigegeben wurde, sollte das Nutzen der Methode für
			// den diese Anwedung dienlich sein
			aktuelleAnzahl--;
		}

		public Integer getKey() {
			return key;
		}

		// vom Skript
		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (o == null || this.getClass() != o.getClass())
				return false;
			return this.key.equals((Key) o);

		}

		@Override
		public int hashCode() {
			return key % aktuelleAnzahl;

		}
	}
}
