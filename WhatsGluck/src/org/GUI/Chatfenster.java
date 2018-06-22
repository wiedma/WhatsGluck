package org.GUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;


public class Chatfenster extends JFrame{

	private static final long serialVersionUID = 7205969481605274530L;
	
	private JPanel rootPanel, kontaktPanel, chatPanel, sendePanel;
	private ArrayList<Kontakt> kontakte;
	private JMenuBar menuBar;
	private JMenu[] menu;
	private JMenuItem[][] menuItem;
	private JScrollPane kontaktScrollPane, chatScrollPane;
	private JTextPane chatPane;
	private JTextField nachrichtField;
	private JButton sendeButton;
	private StyleContext context;
	
	public static final int HOEHE = 500;
	public static final int BREITE = 600;
	public static final int KONTAKT_BREITE = 150;
	public static final int KONTAKT_HOEHE = 50;
	public static final String STANDART_SENDE_TEXT = "Nachricht schreiben";
	public static final int MAX_WORTLAENGE = 30;
	
	public Chatfenster() {
		//TODO: Menu Bar mit Kontaktverwalter
		//MenuBar
		menuBar = new JMenuBar();
		menu = new JMenu[2];
		menuItem = new JMenuItem[2][3];
		
		menu[0] = new JMenu("Datei");
		menuItem[0][0] = new JMenuItem("Einstellungen");
		menuItem[0][1] = new JMenuItem("Beenden");
		menu[0].add(menuItem[0][0]);
		menu[0].add(menuItem[0][1]);
		menuBar.add(menu[0]);
		
		menu[1] = new JMenu("Kontakte");
		menuItem[1][0] = new JMenuItem("Kontakt hinzuf�gen");
		menuItem[1][1] = new JMenuItem("Kontakt l�schen");
		menuItem[1][2] = new JMenuItem("Kontakt bearbeiten");
		menu[1].add(menuItem[1][0]);
		menu[1].add(menuItem[1][1]);
		menu[1].add(menuItem[1][2]);
		menuBar.add(menu[1]);
		
		this.setJMenuBar(menuBar);
		
		//RootPanel 
		rootPanel = new JPanel();
		rootPanel.setLayout(new BorderLayout());
		this.add(rootPanel);
		
		//KontaktPanel
		kontaktPanel = new JPanel() {
			private static final long serialVersionUID = -5282264159280143737L;
			@Override protected void paintComponent(Graphics g){
		        super.paintComponent(g);
		        setBackground(Color.WHITE);
		        setVisible(true);
		    }
		};
		kontaktPanel.setLayout(new GridLayout(0, 1, 0, 2));
		
		kontakte = new ArrayList<Kontakt>();
//		for(int i = 0; i < 100; i++) {
//			Kontakt contact = new Kontakt("Kontakt " + i, "Kontakt " + i, i);
//			contact.setPreferredSize(new Dimension(KONTAKT_BREITE, KONTAKT_HOEHE));
//			if(i < 5) {contact.setNewMessage(true);}
//			kontaktHinzufuegen(contact);
//		}
		
		importiere();
		
		//KontaktScrollPane
		kontaktScrollPane = new JScrollPane(kontaktPanel);
		kontaktScrollPane.getVerticalScrollBar().setUnitIncrement(20);
		kontaktScrollPane.setPreferredSize(new Dimension(KONTAKT_BREITE + 20, HOEHE));
		rootPanel.add(kontaktScrollPane, BorderLayout.WEST);
		
		//ChatPanel
		chatPanel = new JPanel();
		chatPanel.setLayout(new BorderLayout());
		rootPanel.add(chatPanel, BorderLayout.CENTER);
		
		//ChatPane
		context = new StyleContext();
		StyledDocument doc = new DefaultStyledDocument(context);
		chatPane = new JTextPane(doc);
		chatPane.setMaximumSize(new Dimension(200, 200));
		chatPane.setEditable(false);
		
		//ChatScrollPane
		chatScrollPane = new JScrollPane(chatPane);
		chatScrollPane.setViewportView(chatPane);
		chatScrollPane.setMaximumSize(new Dimension(200,200));
		chatPanel.add(chatScrollPane, BorderLayout.CENTER);
		
		//SendePanel
		sendePanel = new JPanel();
		sendePanel.setLayout(new FlowLayout());
		chatPanel.add(sendePanel, BorderLayout.SOUTH);
		
		//NachrichtField
		nachrichtField = new JTextField(STANDART_SENDE_TEXT, 20);
		nachrichtField.setPreferredSize(new Dimension( 200, 30 ) );
		nachrichtField.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				if(nachrichtField.getText().equals(STANDART_SENDE_TEXT)) {
					nachrichtField.setText("");
				}
			}
			
			@Override
			public void focusLost(FocusEvent e) {
				if(nachrichtField.getText().equals("")) {
					nachrichtField.setText(STANDART_SENDE_TEXT);
				}
			}
		});
		nachrichtField.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ENTER,0,false), "EnterPressed");
		nachrichtField.getActionMap().put("EnterPressed", new javax.swing.AbstractAction(){
			public static final long serialVersionUID = -5365806399137704002L;
				public void actionPerformed(ActionEvent e) {
					neueNachrichtSenden();
					nachrichtField.setText("");;
				}
			});
		sendePanel.add(nachrichtField);
		
		//SendeButton
		sendeButton = new JButton("Senden");
		sendeButton.setForeground(Color.WHITE);
		sendeButton.setBackground(new Color(91, 170, 255));
		sendeButton.setFont(new Font("Verdana", Font.BOLD, 12));
		sendeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				neueNachrichtSenden();
			}
		});
		sendePanel.add(sendeButton);
		
		//JFrame
		this.setSize(BREITE, HOEHE);
		this.setTitle("WhatsGluck");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		//WindowListener exportiert bei Schlie�ung des Fensters
		this.addWindowListener(new WindowListener(){
			public void windowClosing(WindowEvent e){
				exportiere();
			}

			@Override
			public void windowOpened(WindowEvent e) {

			}

			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		this.setVisible(true);
		
		//TODO:
		kontaktFensterAnzeigen();
	}
	
	//TODO Im Moment nur Platzhalter f�r eine Sende-Methode, damit Button/Enter-Taste dieselbe Aktion ausf�hrt
	//Sendet die Nachricht 
	public void neueNachrichtSenden() {
		if(!nachrichtField.getText().equals(STANDART_SENDE_TEXT) && !nachrichtField.getText().isEmpty()) {
			neueNachrichtAnzeigen(nachrichtField.getText() + '\n', true);
			nachrichtField.setText(STANDART_SENDE_TEXT);
		}
	}
	
	//Zeigt eine neue Nachricht in der ChatArea an
	//@Param nachricht die neue Nachricht
	//@Param selbst ob man selbst der Absender der Nachricht ist
	public void neueNachrichtAnzeigen(String nachricht, boolean selbst) {
		String[] woerter = nachricht.split(" ");
		
		for(String wort : woerter) {
			int vielfache = wort.length() / MAX_WORTLAENGE;
			StringBuilder builder = new StringBuilder(wort);
			for(int i = 0; i < vielfache; i++) {
				builder.insert((i+1)*MAX_WORTLAENGE, "\n");
			}
			nachricht = nachricht.replace(wort, builder);
		}
		
		StyledDocument doc = chatPane.getStyledDocument();
		Style style = context.getStyle(StyleContext.DEFAULT_STYLE);
		int pos = doc.getLength();
		
		if(selbst) {
			StyleConstants.setAlignment(style, StyleConstants.ALIGN_LEFT);
			StyleConstants.setForeground(style, Color.red);
		}
		else {
			StyleConstants.setAlignment(style, StyleConstants.ALIGN_RIGHT);
			StyleConstants.setForeground(style, Color.blue);
		}
		
		try {
			doc.insertString(doc.getLength(), nachricht, null);
			doc.setParagraphAttributes(pos, nachricht.length(), style, true);
			
		}catch(BadLocationException e) {
			e.printStackTrace();
		}
		
		this.revalidate();
		this.repaint();
	}
	
	public void kontaktFensterAnzeigen(){
		JPanel kfPanel = new JPanel();
		kfPanel.setLayout(new BorderLayout());
		
		String[] kontaktNamen = new String[kontakte.size()];
		for(int i = 0; i < kontaktNamen.length; i++){ kontaktNamen[i] = kontakte.get(i).getContactName(); }
		JList<String> liste = new JList<String>(kontaktNamen);
		JScrollPane listScrollPane = new JScrollPane(liste);
		listScrollPane.setViewportView(liste);
		listScrollPane.setMaximumSize(new Dimension(200,200));
		
		kfPanel.add(listScrollPane, BorderLayout.CENTER);
		
		JButton hinzuf�gen = new JButton("Kontakt hinzuf�gen");
		JButton bearbeiten = new JButton("Kontakt bearbeiten");
		JButton l�schen = new JButton("Kontakt l�schen");
		
		String eingabe = (String) JOptionPane.showInputDialog(
				null, kfPanel, JOptionPane.OK_CANCEL_OPTION);
		
		if(eingabe == ""){
			
		}else{
			
		}
	}
	
	
	public void kontaktHinzufuegen(Kontakt kontakt){
		kontakt.setPreferredSize(new Dimension(KONTAKT_BREITE, KONTAKT_HOEHE));
		kontakte.add(kontakt);
		kontaktPanel.add(kontakt);
	}
	
	public void kontaktEntfernen(Kontakt kontakt){
		kontakte.remove(kontakt);
		kontaktPanel.remove(kontakt);
	}
	
	public Kontakt[] kontakteGeben(){
		return kontakte.toArray(new Kontakt[0]);
	}
	
	public void exportiere(){
		FileLoader.kontakteExportieren(kontakte.toArray(new Kontakt[0]));
	}
	
	public void importiere(){
		for(Kontakt kontakt : FileLoader.kontakteImportieren()){
			kontaktHinzufuegen(kontakt);
		}
	}
}
