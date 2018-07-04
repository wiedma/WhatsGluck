package org.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class Kontakt extends JPanel{
	
	public static final long serialVersionUID = -3266678289400497661L;
	private static final Color defaultColor = new Color(91, 170, 255);
	private static final Color highlightColor = new Color(81, 224, 71);
	private static final String imagePath = "resources\\contactIcon.png";
	
	private String contactName, ipAdress;
	private ImageIcon profileImage = new ImageIcon(imagePath,"profileImage");
	private int id;
	private Color color;
	
	private JPanel contentPanel;
	private JTextArea contactNameField, ipAdressField;
	
	private ArrayList<Nachricht> nachrichten;

	
	public Kontakt(String contactName, String ipAdress, int id){
		this.contactName = contactName;
		this.ipAdress = ipAdress;
		this.color = defaultColor;
		this.setId(id);
		this.setLayout(new BorderLayout());
		this.add(new JLabel(profileImage), BorderLayout.WEST);
		
		//contentPanel
		contentPanel = new JPanel();
		contentPanel.setVisible(true);
		contentPanel.setLayout(new BorderLayout());
		this.add(contentPanel, BorderLayout.CENTER);
		
		//contactNameField
		contactNameField = new JTextArea("\n  "+contactName);
		contactNameField.setOpaque(false);
		contactNameField.setFont(new Font("Verdana", Font.BOLD, 12));
		contactNameField.setForeground(Color.BLACK);
		contactNameField.setEditable(false);
		contentPanel.add(contactNameField, BorderLayout.CENTER);
		
		//ipAdressField
		ipAdressField = new JTextArea("IP: "+ipAdress);
		ipAdressField.setOpaque(false);
		ipAdressField.setFont(new Font("Monospaced", Font.PLAIN, 8));
		ipAdressField.setForeground(Color.WHITE);
		ipAdressField.setEditable(false);
		contentPanel.add(ipAdressField, BorderLayout.SOUTH);
		
		contentPanel.setOpaque(false);
//		this.setOpaque(true);
		
		nachrichten = new ArrayList<Nachricht>();
	}
	
	public Kontakt(String importString) throws NumberFormatException{
		this(importStringSelector(importString, 0), importStringSelector(importString, 1), Integer.parseInt(importStringSelector(importString, 2)));
	}
	
	private static String importStringSelector(String importString, int item) {
		String[] components = importString.split("//");
		try {
			switch(item) {
			case 0: return components[1].substring(6);
			case 1: return components[2].substring(4);
			case 2: return components[0].substring(4);
			default: return "";
			}
		}catch(Exception e) {
			return null;
		}
	}
	
    @Override protected void paintComponent(Graphics g){
        super.paintComponent(g);
        setBackground(color);
        setVisible(true);
    }
	
	public void show(){
		
		
	}

	//Gibt die ID des Kontaktes zurück
	//@Return ID des Kontaktes
	public int getId() {
		return id;
	}

	//Setzt die ID des Kontaktes auf einen bestimmten Wert
	//@Param Neue ID des Kontaktes
	public void setId(int id) {
		this.id = id;
	}
	
	//Gibt den Namen des Kontaktes zurück
	//@Return Name des Kontaktes
	public String getContactName() {
		return contactName;
	}

	//Setzt den Namen des Kontaktes auf einen bestimmten Wert
	//@Param Neuer Name des Kontaktes
	public void setContactName(String contactName) {
		this.contactName = contactName;
		contactNameField.setText("\n  "+contactName);
	}
	
	//Gibt die IP-Adresse des Kontaktes zurück
	//@Return IP-Adresse des Kontaktes
	public String getContactIP() {
		return ipAdress;
	}

	//Setzt die IP-Adresse des Kontaktes auf einen bestimmten Wert
	//@Param Neue IP-Adresse des Kontaktes
	public void setContactIP(String ipAdress) {
		this.ipAdress = ipAdress;
		ipAdressField.setText("IP: "+ipAdress);
	}
	
	//Ändert Farbe des JLabels, wenn neue Nachricht vorhanden
	//@Param Bei true markiert, bei false Standardfarbe
	public void setNewMessage(boolean isNewMessage) {
		color = defaultColor;
		if(isNewMessage) {
			color =highlightColor;
		}
		repaint();
	}
	
	public String toExportString(){
		String s = "";
		s+="ID: " + id + "//";
		s+="NAME: " + contactName + "//";
		s+="IP: " + ipAdress + "//";
		return s;
	}
	
	@Override
	public String toString() {
		return contactName;
	}
	
	public void nachrichtHinzufuegen(String text, boolean vonMir) {
		nachrichten.add(new Nachricht(text, vonMir));
	}
	
	public String[] nachrichtenExport() {
		String[] nachrichtenStrings = new String[nachrichten.size()];
		for(int i = 0; i < nachrichten.size(); i++) {
			String exportString;
			Nachricht currentNachricht = nachrichten.get(i);
			String prefix;
			if(currentNachricht.istVonMir()) {
				prefix = "//++//";
			}else {
				prefix = "//--//";
			}
			exportString = prefix + currentNachricht.textGeben();
			nachrichtenStrings[i] = exportString;
		}
		return nachrichtenStrings;
	}
	
	public Nachricht[] nachrichtenGeben() {
		return nachrichten.toArray(new Nachricht[0]);
	}
	
}
