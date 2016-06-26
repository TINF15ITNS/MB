package MessageServer;

public class Key {
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
		// diesen Anwedungszweck dienlich sein
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