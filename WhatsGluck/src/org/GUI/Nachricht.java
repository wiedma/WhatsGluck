package org.GUI;

public class Nachricht {
	
	private String text;
	private boolean vonMir;
	
	public Nachricht(String text, boolean vonMir) {
		this.text = text;
		this.vonMir = vonMir;
	}
	
	public String textGeben() {
		return text;
	}
	
	public boolean istVonMir() {
		return vonMir;
	}

}
