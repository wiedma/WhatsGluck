package org.Main;
import org.GUI.Chatfenster;
import org.GUI.Kontakt;

public class Main {
	
	public static void main(String[] args) {
		new Chatfenster();
		Kontakt k1 = new Kontakt("Benedikt", "127.0.0.1", 0);
		Kontakt k2 = new Kontakt(k1.toString());
	}

}
