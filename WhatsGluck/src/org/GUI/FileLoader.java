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
			out.print(kontakt.toString());
			out.println();
		}
		out.flush();
		out.close();
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
				kontaktList.add(new Kontakt(kontaktString));
			}
			in.close();
			return kontaktList.toArray(new Kontakt[0]);
		} catch(IOException e){
			JOptionPane.showMessageDialog(null, "Die Datei konnte nicht gelesen werden");
			return null;
		}
		
	}

}
