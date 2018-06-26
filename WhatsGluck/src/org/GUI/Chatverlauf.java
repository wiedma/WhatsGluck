package org.GUI;

import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

public class Chatverlauf {
	
	private int kontaktID;
	private StyledDocument doc;
	private String[] nachrichten;
	
	public Chatverlauf(int kontaktID, StyledDocument doc) {
		this.kontaktID = kontaktID;
		this.doc = doc;
		try {
			nachrichten = doc.getText(0, doc.getLength()).split("\n");
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String toString() {
		String s = "";
		s += "ID: " + kontaktID + "//";
		for(String nachricht : nachrichten) {
			s += nachricht + "//";
		}
		return s;
	}

}
