package messageServer;

import java.net.InetAddress;
import java.util.Set;

public interface Customer {

}

// das sind die Objekte die in der HashMap gespeichert werden sollen, die die Informationen über Produzenten und Konsumenten halten
class ConsumerMS implements Customer {
	String name;
	InetAddress address;
	final int clientID;
	Set<Integer> enteredProducers;

	public ConsumerMS(int id, String[] producers) {
		clientID = id;
		// ermittle aus dem String array die ids, auf die der Komsument sich eingeschrieben hat und stecke sie in den set

	}
}

class ProducerMS implements Customer {
	public ProducerMS() {

	}
}
