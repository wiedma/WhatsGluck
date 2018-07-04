package org.GUI;
import java.io.*;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public final class FileLoader {
	
	private FileLoader(){}
	
	public static void kontakteExportieren(Kontakt[] kontakte){
		System.out.println("Export...");
		File kontaktFile = new File("Kontakte.txt");
		PrintWriter out;
		//Erzeuge neue Datei
		if(kontaktFile.exists()){
			kontaktFile.delete();
		}
		try{
			kontaktFile.createNewFile();
			//Erzeuge PrintWriter
			out = new PrintWriter(kontaktFile.getAbsolutePath());
		} catch(IOException e){
			JOptionPane.showMessageDialog(null, "Die Datei konnte nicht erzeugt werden");
			return;
		}
		
		for(Kontakt kontakt : kontakte){
			out.print(kontakt.toExportString());
			out.println();
		}
		out.flush();
		out.close();
		
		PrintWriter outVerlauf;
		File verlaufFile = new File("Chatverlauf.txt");
		if(verlaufFile.exists()) {
			verlaufFile.delete();
		}
		try {
			verlaufFile.createNewFile();
			
			outVerlauf = new PrintWriter(verlaufFile.getAbsolutePath());
		} catch(IOException e) {
			JOptionPane.showMessageDialog(null, "Die Datei konnte nicht erzeugt werden");
			return;
		}
		
		for(Kontakt kontakt : kontakte) {
			outVerlauf.println("//ID//" + kontakt.getId());
			for(String s : kontakt.nachrichtenExport()) {
				outVerlauf.print(s);
			}
		}
		
		outVerlauf.flush();
		outVerlauf.close();
	}
	
	public static Kontakt[] kontakteImportieren(){
		File kontaktFile = new File("Kontakte.txt");
		FileReader read;
		BufferedReader in;
		try{
			kontaktFile.createNewFile();
			//Lese die Datei
			read = new FileReader(kontaktFile.getAbsolutePath());
			in = new BufferedReader(read);
			ArrayList<Kontakt> kontaktList = new ArrayList<Kontakt>();
			while(in.ready()){
				String kontaktString = in.readLine();
				try {
					kontaktList.add(new Kontakt(kontaktString));
				} catch(NumberFormatException e) {
					
				}
			}
			in.close();
			
			File chatFile = new File("Chatverlauf.txt");
			FileReader readChat = new FileReader(chatFile.getAbsolutePath());
			BufferedReader inChat = new BufferedReader(readChat);
			Kontakt currentKontakt = null;
			while(inChat.ready()) {
				String inString = inChat.readLine();
				String prefix = inString.substring(0, 6);
				if(prefix.equals("//ID//")) {
					int kontaktID = Integer.parseInt(inString.substring(6, inString.length()));
					currentKontakt = kontaktList.get(kontaktID - 1);
				}
				else if(prefix.equals("//++//")) {
					String nachrichtText = inString.substring(6, inString.length());
					currentKontakt.nachrichtHinzufuegen(nachrichtText+"\n", true);
				}
				else if(prefix.equals("//--//")) {
					String nachrichtText = inString.substring(6, inString.length());
					currentKontakt.nachrichtHinzufuegen(nachrichtText+"\n", false);
				}
			}
			
			inChat.close();
			
			return kontaktList.toArray(new Kontakt[0]);
		} catch(IOException e){
			JOptionPane.showMessageDialog(null, "Die Kontakt-Datei konnte nicht gelesen werden");
			return null;
		}
		
	}
		
		

}
