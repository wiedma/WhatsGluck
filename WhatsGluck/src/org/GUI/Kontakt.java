package org.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class Kontakt extends JPanel {
	
	public static final long serialVersionUID = -3266678289400497661L;
	private static final Color defaultColor = new Color(91, 170, 255);
	private static final Color highlightColor = new Color(81, 224, 71);
	private static final String imagePath = "resources\\contactIcon.png";
	private String contactName, ipAdress;
	private ImageIcon profileImage = new ImageIcon(imagePath,"profileImage");
	private int id;
	private Color color;

	
	public Kontakt(String contactName, String ipAdress, int id){
		this.contactName = contactName;
		this.ipAdress = ipAdress;
		this.color = defaultColor;
		this.setId(id);
		show();
	}
	
	public Kontakt(String importString){
		String[] components = importString.split("//");
		id = Integer.parseInt(components[0].substring(4));
		contactName = components[1].substring(6);
		ipAdress = components[2].substring(4);
		color = defaultColor;
		show();
	}
	
    @Override protected void paintComponent(Graphics g){
        super.paintComponent(g);
        setBackground(color);
        setVisible(true);
    }
	
	public void show(){
		this.setLayout(new BorderLayout());
		this.add(new JLabel(profileImage), BorderLayout.WEST);
		
		//contentPanel
		JPanel contentPanel = new JPanel();
		contentPanel.setVisible(true);
		contentPanel.setLayout(new BorderLayout());
		this.add(contentPanel, BorderLayout.CENTER);
		
		//contactNameField
		JTextArea contactNameField = new JTextArea("\n  "+contactName);
		contactNameField.setOpaque(false);
		contactNameField.setFont(new Font("Verdana", Font.BOLD, 12));
		contactNameField.setForeground(Color.BLACK);
		contactNameField.setEditable(false);
		contentPanel.add(contactNameField, BorderLayout.CENTER);
		
		//ipAdressField
		JTextArea ipAdressField = new JTextArea("IP: "+ipAdress);
		ipAdressField.setOpaque(false);
		ipAdressField.setFont(new Font("Monospaced", Font.PLAIN, 8));
		ipAdressField.setForeground(Color.WHITE);
		ipAdressField.setEditable(false);
		contentPanel.add(ipAdressField, BorderLayout.SOUTH);
		
		contentPanel.setOpaque(false);
		this.setOpaque(true);
		
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
	
	@Override
	public String toString(){
		String s = "";
		s+="ID: " + id + "//";
		s+="NAME: " + contactName + "//";
		s+="IP: " + ipAdress + "//";
		return s;
	}
	
}
