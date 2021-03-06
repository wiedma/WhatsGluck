package org.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.math.BigInteger;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class Kontakt extends JPanel{
	
	public static final long serialVersionUID = -3266678289400497661L;
	private static final Color defaultColor = new Color(91, 170, 255);
	private static final Color highlightColor = new Color(81, 224, 71);
	private static final String imagePathOn = "resources\\contactIconOn.png";
	private static final String imagePathOff = "resources\\contactIconOff.png";
	
	private String contactName, ipAdress;
	private int id;
	private Color color;
	
	private JPanel contentPanel;
	private JTextArea contactNameField, ipAdressField;
	private JLabel iconLabel;
	private ImageIcon profileImageOff, profileImageOn;
	
	private ArrayList<Nachricht> nachrichten;
	private BigInteger publicKey, privateKey, publicModul, privateModul;
	private boolean online;
	
	public Kontakt(String contactName, String ipAdress, int id){
		this.contactName = contactName;
		this.ipAdress = ipAdress;
		this.color = defaultColor;
		this.setId(id);
		this.setLayout(new BorderLayout());
		profileImageOff = new ImageIcon(imagePathOff,"profileImage");
		profileImageOn = new ImageIcon(imagePathOn,"profileImage");
		iconLabel = new JLabel(profileImageOff);
		JPanel space = new JPanel();
		space.setOpaque(false);
		space.setLayout(new BorderLayout());
		space.add(new JLabel("   "), BorderLayout.CENTER);
		space.add(iconLabel, BorderLayout.EAST);
		this.add(space, BorderLayout.WEST);
		
		
		//contentPanel
		contentPanel = new JPanel();
		contentPanel.setVisible(true);
		contentPanel.setLayout(new BorderLayout());
		this.add(contentPanel, BorderLayout.CENTER);
		
		//contactNameField
		contactNameField = new JTextArea("\n  "+contactName);
		contactNameField.setOpaque(false);
		contactNameField.setFont(new Font("SansSerif", Font.BOLD, 13));
		contactNameField.setForeground(Color.BLACK);
		contactNameField.setEditable(false);
		contactNameField.setFocusable(false);
		contentPanel.add(contactNameField, BorderLayout.CENTER);
		
		//ipAdressField
		ipAdressField = new JTextArea("IP: "+ipAdress);
		ipAdressField.setOpaque(false);
		ipAdressField.setFont(new Font("Monospaced", Font.PLAIN, 8));
		ipAdressField.setForeground(Color.WHITE);
		ipAdressField.setEditable(false);
		contentPanel.add(ipAdressField, BorderLayout.SOUTH);
		contentPanel.setOpaque(false);
		
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
        
        //setBackground(color);
        Dimension arcs = new Dimension(15,15);
        int width = getWidth();
        int height = getHeight();
        Graphics2D graphics = (Graphics2D) g;
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //Draws the rounded opaque panel with borders.
        graphics.setColor(color);
        graphics.fillRoundRect(0, 0, width-1, height-1, arcs.width, arcs.height);//paint background
        graphics.setColor(new Color(120,120,120));
        graphics.drawRoundRect(0, 0, width-1, height-1, arcs.width, arcs.height);//paint border

        setVisible(true);
    }
	
	public void show(){
		
		
	}

	//Gibt die ID des Kontaktes zur�ck
	//@Return ID des Kontaktes
	public int getId() {
		return id;
	}

	//Setzt die ID des Kontaktes auf einen bestimmten Wert
	//@Param Neue ID des Kontaktes
	public void setId(int id) {
		this.id = id;
	}
	
	//Gibt den Namen des Kontaktes zur�ck
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
	
	//Gibt die IP-Adresse des Kontaktes zur�ck
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
	
	//�ndert Farbe des JLabels, wenn neue Nachricht vorhanden
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
	
	public void addMouseListenerToContact(MouseListener m) {
		this.addMouseListener(m);
		contentPanel.addMouseListener(m);
		ipAdressField.addMouseListener(m);
		contactNameField.addMouseListener(m);
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
	
	public void setPublicKey(BigInteger key) {
		this.publicKey = key;
	}
	
	public void setPrivateKey(BigInteger key) {
		this.privateKey = key;
	}
	
	public void setPublicModul(BigInteger modul) {
		this.publicModul = modul;
	}
	
	public void setPrivateModul(BigInteger modul) {
		this.privateModul = modul;
	}
	
	public BigInteger getPublicKey() {
		return publicKey;
	}
	
	public BigInteger getPrivateKey() {
		return privateKey;
	}
	
	public BigInteger getPublicModul() {
		return publicModul;
	}
	
	public BigInteger getPrivateModul() {
		return privateModul;
	}
	
	public void setOnline(boolean online) {
		if(online) {
			iconLabel.setIcon(profileImageOn);
		}else {
			iconLabel.setIcon(profileImageOff);
		}
		this.online = online;
	}
	
	public boolean isOnline() {
		return online;
	}
	
}
