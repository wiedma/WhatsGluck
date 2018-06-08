package org.GUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.*;

public class Chatfenster extends JFrame{

	private static final long serialVersionUID = 7205969481605274530L;
	
	private JPanel rootPanel, kontaktPanel, chatPanel, sendePanel;
	private JScrollPane kontaktScrollPane, chatScrollPane;
	private JTextArea chatArea;
	private JTextField nachrichtField;
	private JButton sendeButton;
	
	public static final int hoehe = 500;
	public static final int breite = 600;
	public static final int kontaktBreite = 100;
	public static final int kontaktHoehe = 30;
	public static final String standartSendeText = "Nachricht schreiben";
	
	public Chatfenster() {
		//RootPanel 
		rootPanel = new JPanel();
		rootPanel.setLayout(new BorderLayout());
		this.add(rootPanel);
		
		//KontaktPanel
		kontaktPanel = new JPanel();
		kontaktPanel.setLayout(new GridLayout(0, 1));
		
		for(int i = 0; i < 200; i++) {
			JLabel label = new JLabel("Kontakt " + i);
			label.setPreferredSize(new Dimension(kontaktBreite, kontaktHoehe));
			kontaktPanel.add(label);
		}
		
		//KontaktScrollPane
		kontaktScrollPane = new JScrollPane(kontaktPanel);
		kontaktScrollPane.setPreferredSize(new Dimension(kontaktBreite + 20, hoehe));
		rootPanel.add(kontaktScrollPane, BorderLayout.WEST);
		
		//ChatPanel
		chatPanel = new JPanel();
		chatPanel.setLayout(new BorderLayout());
		rootPanel.add(chatPanel, BorderLayout.CENTER);
		
		//ChatArea
		chatArea = new JTextArea();
		chatArea.setEditable(false);
		
		//ChatScrollPane
		chatScrollPane = new JScrollPane(chatArea);
		chatPanel.add(chatScrollPane, BorderLayout.CENTER);
		
		//SendePanel
		sendePanel = new JPanel();
		sendePanel.setLayout(new FlowLayout());
		chatPanel.add(sendePanel, BorderLayout.SOUTH);
		
		//NachrichtField
		nachrichtField = new JTextField(standartSendeText, 20);
		nachrichtField.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				if(nachrichtField.getText().equals(standartSendeText)) {
					nachrichtField.setText("");
				}
			}
			
			@Override
			public void focusLost(FocusEvent e) {
				if(nachrichtField.getText().equals("")) {
					nachrichtField.setText(standartSendeText);
				}
			}
		});
		sendePanel.add(nachrichtField);
		
		//SendeButton
		sendeButton = new JButton("Senden");
		sendeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!nachrichtField.getText().equals(standartSendeText)) {
					neueNachrichtAnzeigen(nachrichtField.getText() + '\n', true);
					nachrichtField.setText(standartSendeText);
				}
			}
		});
		sendePanel.add(sendeButton);
		
		//JFrame
		this.setSize(breite, hoehe);
		this.setTitle("WhatsGluck");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	//Zeigt eine neue Nachricht in der ChatArea an
	//@Param nachricht die neue Nachricht
	//@Param selbst ob man selbst der Absender der Nachricht ist
	public void neueNachrichtAnzeigen(String nachricht, boolean selbst) {
		chatArea.append(nachricht);
	}
}
