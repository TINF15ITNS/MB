package Producer;

import MessageServer.Key;

public class Producer {
	String name;
	Key key;

	public Producer(String n) {
		System.out.println("Bin im Konstruktor von Producer");
		this.name = n;
	}

	public boolean setKey(Key k) {
		key = k;
		return true;
	}

}
