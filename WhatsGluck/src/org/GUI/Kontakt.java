package org.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class Kontakt extends JPanel {
	
	//TODO: Kontakt-Bild implememtieren? und Farbmischung anpassen!
	private static final long serialVersionUID = -3266678289400497661L;
	private static final Color defaultColor = Color.GRAY;
	//private static final String imagePath = "";
	private String contactName, ipAdress;
	//private ImageIcon profileImage = new ImageIcon(imagePath,"profileImage");
	private int id;
	private Color color;
	
	public Kontakt(String contactName, String ipAdress, int id){
		this(contactName, ipAdress, id, defaultColor);
	}
	
	public Kontakt(String contactName, String ipAdress, int id, Color color){
		this.contactName = contactName;
		this.ipAdress = ipAdress;
		this.color = color;
		this.setId(id);
		show();
	}
	
    @Override protected void paintComponent(Graphics g){
        super.paintComponent(g);
        setBackground(color);
        setVisible(true);
    }
	
	public void show(){
		this.setOpaque(true);
		this.setLayout(new BorderLayout());
		
		JTextArea contactNameField = new JTextArea(contactName);
		contactNameField.setOpaque(false);
		contactNameField.setFont(new Font("Serif", Font.BOLD, 12));
		contactNameField.setForeground(Color.BLACK);
		contactNameField.setEditable(false);
		this.add(contactNameField, BorderLayout.CENTER);
		
		//this.add(profileImage, BorderLayout.WEST);
		
		JTextArea ipAdressField = new JTextArea("IP: "+ipAdress);
		ipAdressField.setOpaque(false);
		ipAdressField.setFont(new Font("Monospaced", Font.PLAIN, 8));
		ipAdressField.setForeground(Color.WHITE);
		ipAdressField.setEditable(false);
		this.add(ipAdressField, BorderLayout.SOUTH);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
}
