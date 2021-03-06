package org.GUI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.Key;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import org.Security.AESEncrypt;
import org.Security.RsaEncrypt;


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
	private Kontakt aktiverKontakt;
	private ServerSocket serverSocket;
	private Thread socketThread;
	private boolean listening = true;
	private ImageIcon icon;
	
	
	public static final int HOEHE = 500;
	public static final int BREITE = 600;
	public static final int KONTAKT_BREITE = 150;
	public static final int KONTAKT_HOEHE = 50;
	public static final String STANDART_SENDE_TEXT = "Nachricht schreiben...";
	public static final int MAX_WORTLAENGE = 30;
	public static final int PORT_NUMBER = 1234; //Für Test: 60000
	
	public Chatfenster() {
		
		//MenuBar
		menuBar = new JMenuBar();
		menuBar.setBackground(Color.WHITE);
		menu = new JMenu[2];
		menuItem = new JMenuItem[2][3];
		
		ActionListener actionMenu = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				switch(arg0.getActionCommand()) {
				case "Kontakt bearbeiten": kontaktFensterBearbeiten(); break;
				case "Kontakt löschen": kontaktFensterLoeschen(); break;
				case "Kontakt hinzufügen": kontaktFensterHinzufuegen(); break;
				case "Info": infoFenster(); break;
				case "Speichern": exportiere(); break;
				case "Beenden": closeWindow();
				default: System.out.println("Error: Could not find source."); break;
				}
			}
		};
		
		menu[0] = new JMenu("Datei");
		menuItem[0][0] = new JMenuItem("Info");
		menuItem[0][0].setBackground(Color.WHITE);
		menuItem[0][0].setOpaque(true);
		menuItem[0][0].addActionListener(actionMenu);
		menuItem[0][1] = new JMenuItem("Speichern");
		menuItem[0][1].setBackground(Color.WHITE);
		menuItem[0][1].setOpaque(true);
		menuItem[0][1].addActionListener(actionMenu);
		menuItem[0][2] = new JMenuItem("Beenden");
		menuItem[0][2].setBackground(Color.WHITE);
		menuItem[0][2].setOpaque(true);
		menuItem[0][2].addActionListener(actionMenu);
		menu[0].add(menuItem[0][0]);
		menu[0].add(menuItem[0][1]);
		menu[0].add(menuItem[0][2]);
		menuBar.add(menu[0]);
		
		menu[1] = new JMenu("Kontakte");
		menuItem[1][0] = new JMenuItem("Kontakt hinzufügen");
		menuItem[1][0].setActionCommand("Kontakt hinzufügen");
		menuItem[1][0].setBackground(Color.WHITE);
		menuItem[1][0].setOpaque(true);
		menuItem[1][0].addActionListener(actionMenu);
		menuItem[1][1] = new JMenuItem("Kontakt löschen");
		menuItem[1][1].setActionCommand("Kontakt löschen");
		menuItem[1][1].setBackground(Color.WHITE);
		menuItem[1][1].setOpaque(true);
		menuItem[1][1].addActionListener(actionMenu);
		menuItem[1][2] = new JMenuItem("Kontakt bearbeiten");
		menuItem[1][2].setActionCommand("Kontakt bearbeiten");
		menuItem[1][2].setBackground(Color.WHITE);
		menuItem[1][2].setOpaque(true);
		menuItem[1][2].addActionListener(actionMenu);
		menu[1].add(menuItem[1][0]);
		menu[1].add(menuItem[1][1]);
		menu[1].add(menuItem[1][2]);
		menuBar.add(menu[1]);
		
		this.setJMenuBar(menuBar);
		
		//RootPanel 
		rootPanel = new JPanel();
		rootPanel.setLayout(new BorderLayout());
		this.add(rootPanel);
		
		//KontaktFlowPanel
		JPanel kontaktFlowPanel = new JPanel();
		//kontaktFlowPanel.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
		kontaktFlowPanel.setLayout(new FlowLayout());
		
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
		kontaktFlowPanel.add(kontaktPanel);
		
		kontakte = new ArrayList<Kontakt>();

		//KontaktScrollPane
		kontaktScrollPane = new JScrollPane(kontaktFlowPanel);
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
		icon = new ImageIcon("resources\\Icon.png");
        this.setIconImage(icon.getImage());
		
		//WindowListener exportiert bei Schließung des Fensters
		this.addWindowListener(new WindowListener(){
			public void windowClosing(WindowEvent e){
				abmelden();
				exportiere();
			}

			@Override
			public void windowOpened(WindowEvent e) {}

			@Override
			public void windowClosed(WindowEvent e) {	}

			@Override
			public void windowIconified(WindowEvent e) {}

			@Override
			public void windowDeiconified(WindowEvent e) {}

			@Override
			public void windowActivated(WindowEvent e) {}

			@Override
			public void windowDeactivated(WindowEvent e) {}
		});
		
		try {
			serverSocket = new ServerSocket(60000);
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(this, "Port 60000 kann nicht verwendet werden!", "Error", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		
		
		socketThread = new Thread() {
			public void run() {
				
				while(listening) {
					try {
						Socket neuerKontakt = serverSocket.accept();
						nachrichtEmpfangen(neuerKontakt);
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				}
			}
		};
		socketThread.start();
		
		//Importiere Kontakte und Chatverläufe im GUI-Thread
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				importiere();
				
				for(Kontakt k : kontakte) {
					schluesselPaarSenden(k);
				}
				
				try {
					aktiverKontaktSetzen(kontakte.get(0));
				} catch(IndexOutOfBoundsException e) {
					e.printStackTrace();
				}
			}
		});
		
		this.setVisible(true);
	}
	
	public void neueNachrichtSenden() {
		if(!nachrichtField.getText().equals(STANDART_SENDE_TEXT) && !nachrichtField.getText().isEmpty() && aktiverKontakt.isOnline()) {
			
			try {
				Socket sendeSocket = new Socket();
				sendeSocket.connect(new InetSocketAddress(aktiverKontakt.getContactIP(), PORT_NUMBER), 150);
				PrintWriter out = new PrintWriter(sendeSocket.getOutputStream());
				String klartext = nachrichtField.getText();
				Key key = AESEncrypt.schlüsselErzeugen();
				String nachricht = AESEncrypt.nachrichtVerschlüsseln(key, klartext);
				String schlüssel = RsaEncrypt.nachrichtVerschlüsseln(aktiverKontakt.getPublicKey(),
						aktiverKontakt.getPublicModul(), AESEncrypt.keyToString(key));
				
				String message = schlüssel + "//" + nachricht;
				
				out.println(message);
				
				out.flush();
				
				sendeSocket.close();
			} catch (UnknownHostException e) {
				e.printStackTrace();
				return;
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
			
			neueNachrichtAnzeigen(nachrichtField.getText() + '\n', true);
			aktiverKontakt.nachrichtHinzufuegen(nachrichtField.getText() + '\n', true);

			nachrichtField.setText(STANDART_SENDE_TEXT);
		}
	}
	
	public void nachrichtEmpfangen(Socket sender) throws IOException {
		String senderIP = sender.getRemoteSocketAddress().toString();
		senderIP = senderIP.replace("/", "");
		senderIP = senderIP.substring(0, senderIP.indexOf(":"));
		InputStream in = sender.getInputStream();
		InputStreamReader inRead = new InputStreamReader(in);
		BufferedReader read = new BufferedReader(inRead);
		String nachricht = "";
		while(!read.ready()) {}
		while(read.ready()) {
			nachricht += read.readLine();
		}
		
		Kontakt senderKontakt = null;
		for(Kontakt k : kontakte) {
			if(k != null && k.getContactIP() != null && k.getContactIP().equals(senderIP)) {
				senderKontakt = k;
			}
		}
		
		if(senderKontakt == null) {
			int n = JOptionPane.showConfirmDialog(this, "Sie haben eine Nachricht von einem Unbekannten erhalten.\n"
					+ "Möchten Sie diese IP-Adresse zu ihren Kontakten aufnehmen?\n"
					+ "IP: " + senderIP);
			
			
			if(n != JOptionPane.YES_OPTION) {
				return;
			}
			else {
				senderKontakt = kontaktFensterHinzufuegen(senderIP);
			}
			
			if(senderKontakt == null) {
				return;
			}
		}
		try {
			if(nachricht.substring(0, 15).equals("//##KeyPair##//")) {
				schluesselPaarEmpfangen(nachricht.substring(15, nachricht.length()), senderKontakt);
				return;
			}
			else if(nachricht.substring(0, 15).equals("//##Offline##//")) {
				senderKontakt.setOnline(false);
				return;
			}
			else if(nachricht.substring(0, 15).equals("//%%KeyPair%%//")) {
				schluesselPaarAkzeptieren(nachricht.substring(15, nachricht.length()), senderKontakt);
				return;
			}
		}catch (StringIndexOutOfBoundsException e) {}
		
		String[] chunks = nachricht.split("//");
		Key aesKey = AESEncrypt.stringToKey(RsaEncrypt.nachrichtEntschlüsseln(senderKontakt.getPrivateKey(),
				senderKontakt.getPrivateModul(), chunks[0]));
		nachricht = AESEncrypt.nachrichtEntschlüsseln(aesKey, chunks[1]);
		nachricht += "\n";
		senderKontakt.nachrichtHinzufuegen(nachricht, false);
		senderKontakt.setNewMessage(true);
		playNewMessage();
		
		this.aktiverKontaktSetzen(aktiverKontakt);
		
		System.out.println("Message From: " + senderIP);
		System.out.println(nachricht);
	}
	
	/**Zeigt eine neue Nachricht in der ChatArea an
	*@Param nachricht die neue Nachricht
	*@Param selbst ob man selbst der Absender der Nachricht ist*/
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
			StyleConstants.setForeground(style, Color.black);
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
		JScrollBar scrollBar = chatScrollPane.getVerticalScrollBar();
		scrollBar.setValue(scrollBar.getMaximum());
//		this.revalidate();
		this.repaint();
	}
	
	//Spielt eine Sound-Datei bei Eintreffen einer neuen Nachricht
	public void playNewMessage() {
		try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("resources\\MessageSound.wav"));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);clip.start();
        } catch(Exception e) {
        	System.out.println("Error with playing sound."); 
        	e.printStackTrace(); 
        }
	}
	
	//Öffnet ein kleines Informationsfenster
	public void infoFenster() {
		JTextArea info = new JTextArea();
		info.setEditable(false);
		info.setFocusable(false);
		info.setOpaque(false);
		info.setText("Dieses Chat-Programm 'WhatsGluck' wurde in JAVA im Rahmen eines Informatik-Projektes entwickelt. \n\n"
				+ "Die Nachrichten werden mithilfe einer Hybrid-Verschlüsselung aus RSA und AES übertragen. \n"
				+"Damit ist das Abhören von Dritten (ohne erheblichen Aufwand) bei der Datenübertragung ausgeschlossen.\n"
				+"Eine Verbindung wird mithilfe der IP-Adresse des Adressaten über Sockets aufgebaut.\n"
				+"Dabei ist es möglich, mit mehreren Personen parallel zu kommunizieren. \n"
				+"Chatverläufe und Kontaktdaten können lokal abgespeichert und wiederverwendet werden. \n\n"
				+"Autoren: \n\n\t Marco Wiedmann \n\t Benedikt Naumann\n\n"
				+"Alle Rechte vorbehalten.\n"
				+"Es wird keine Haftung für Schäden oder anderweitige Verluste bei der Nutzung dieser Applikation übernommen. \n\n"
				+"Source-Code einsehen: https:www.github.com/wiedma/WhatsGluck");
		Object[] message = {info};
		Object[] options = {"OK"};
		
		JOptionPane.showOptionDialog(this, message, "Info", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
	}
	
	//Zeigt das Dialog-Fenster zum Hinzufügen eines Kontaktes an
	public void kontaktFensterHinzufuegen(){
		JTextField name = new JTextField();
		JTextField ip = new JTextField();
        Object[] message = {"Name:", name, "IP-Adresse:", ip};
        Object[] options = {"Hinzufügen", "Abbrechen"};
        int option = 0;

        while(option == 0 && name.getText().length() == 0 && ip.getText().length() == 0)
        	option = JOptionPane.showOptionDialog(this, message, "Kontakt hinzufügen...", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if(option == 0) {
        	Kontakt k = new Kontakt(name.getText(), ip.getText(), kontakte.size());
        	kontaktHinzufuegen(k);
        	schluesselPaarSenden(k);
        	try {Thread.sleep(200);} catch(Exception e) {}
        }
	}
	
	//Zeigt das Dialog-Fenster zum Hinzufügen eines Kontaktes an
	public Kontakt kontaktFensterHinzufuegen(String IP){
		JTextField name = new JTextField();
		JTextField ip = new JTextField();
		ip.setText(IP);
        Object[] message = {"Name:", name, "IP-Adresse:", ip};
        Object[] options = {"Hinzufügen", "Abbrechen"};
        int option = 0;
        do {
        	option = JOptionPane.showOptionDialog(this, message, "Kontakt hinzufügen...", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        }while(option == 0 && name.getText().length() == 0 && ip.getText().length() == 0);
        if(option == 0) {
        	Kontakt k = new Kontakt(name.getText(), ip.getText(), kontakte.size());
        	kontaktHinzufuegen(k);
        	schluesselPaarSenden(k);
        	try {Thread.sleep(200);} catch(Exception e) {}
        	return k;
        }
        return null;
	}
	
	//Zeigt das Dialog-Fenster zum Löschen eines Kontaktes an
	public void kontaktFensterLoeschen() {
		String[] kontaktNamen = new String[kontakte.size()];
		for(int i = 0; i < kontaktNamen.length; i++){ kontaktNamen[i] = kontakte.get(i).getContactName(); }
		JList<String> liste = new JList<String>(kontaktNamen);
		liste.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		JScrollPane listScrollPane = new JScrollPane(liste);
		listScrollPane.setViewportView(liste);
		listScrollPane.setMaximumSize(new Dimension(200,200));
		Object[] message = {listScrollPane, "\nHinweis: Alle Daten zu diesem Kontakt werden gelöscht!\n"};
		Object[] options = {"Löschen", "Abbrechen"};
		int option = 0;
		
		while(option == 0 && liste.getSelectedIndices().length == 0)
			option = JOptionPane.showOptionDialog(this, message, "Kontakt löschen...", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
		
		if(option == 0) {
			int[] indices = liste.getSelectedIndices();
			for(int index : indices) kontaktEntfernen(kontakte.get(index));
		}
	}
	
	//Zeigt das Dialog-Fenster zum Bearbeiten eines Kontaktes an
	public void kontaktFensterBearbeiten() {
		JTextField name = new JTextField();
		JTextField ip = new JTextField();
//		for(int i = 0; i < kontaktNamen.length; i++){ kontaktNamen[i] = kontakte.get(i).getContactName(); }
		JList<Kontakt> liste = new JList<Kontakt>(kontakte.toArray(new Kontakt[0]));
		liste.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		liste.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				name.setText(liste.getSelectedValue().getContactName());
				ip.setText(liste.getSelectedValue().getContactIP());
			}
			@Override
			public void mouseEntered(MouseEvent arg0) {}
			@Override
			public void mouseExited(MouseEvent arg0) {}
			@Override
			public void mousePressed(MouseEvent arg0) {}
			@Override
			public void mouseReleased(MouseEvent arg0) {}
		});
		JScrollPane listScrollPane = new JScrollPane(liste);
		listScrollPane.setViewportView(liste);
		listScrollPane.setMaximumSize(new Dimension(200,200));
		
		JCheckBox box = new JCheckBox("Chatverlauf übernehmen");
		box.setSelected(true);
        Object[] message = {listScrollPane, "Name:", name, "IP-Adresse:", ip, box};
        Object[] options = {"Ändern", "Fertig"};
		
		int option = JOptionPane.showOptionDialog(this, message, "Kontakt bearbeiten...", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
		
		if(option == 0) {
			int index = liste.getSelectedIndex();
			if(index >= 0 && ip.getText().length() > 0 && name.getText().length() > 0) {
				kontaktBearbeiten(kontakte.get(index), name.getText(), ip.getText(), box.isSelected());
			}
			kontaktFensterBearbeiten();
			kontaktScrollPane.updateUI();
			rootPanel.repaint();
		}
	}
	
	//Fügt einen Kontakt hinzu
	//@Param kontakt der neue Kontakt
	public void kontaktHinzufuegen(Kontakt kontakt){
		kontakt.setPreferredSize(new Dimension(KONTAKT_BREITE, KONTAKT_HOEHE));
		kontakt.setMaximumSize(new Dimension(KONTAKT_BREITE, KONTAKT_HOEHE));
		kontakt.addMouseListenerToContact(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				aktiverKontaktSetzen(kontakt);
			}

			@Override
			public void mousePressed(MouseEvent e) {}

			@Override
			public void mouseReleased(MouseEvent e) {}

			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {}
			
		});
		kontakte.add(kontakt);
		kontakt.setId(kontakte.size());
		kontaktPanel.add(kontakt);
		kontaktScrollPane.updateUI();
		rootPanel.repaint();
		
//		SwingUtilities.invokeLater(new Runnable() {
//			public void run() {
//				schluesselPaarSenden(kontakt);
//			}
//		});
	}
	
	//Löscht einen Kontakt
	//@Param kontakt der zu löschende Kontakt
	public void kontaktEntfernen(Kontakt kontakt){
		kontakte.remove(kontakt);
		kontaktPanel.remove(kontakt);
		for(int index = 0; index < kontakte.size(); index++) kontakte.get(index).setId(index+1);
		kontaktScrollPane.updateUI();
		rootPanel.repaint();
	}
	
	/**Bearbeitet einen Kontakt (vgl. kontaktFensterBearbeiten() )
	*@Param kontakt der zu bearbeitende Kontakt
	*@Param name der neue Name des Kontaktes
	*@Param ip die neue IP-Adresse des Kontaktes
	*@Param chatBeibehalten ob der Chatverlauf beibehalten werden soll*/
	public void kontaktBearbeiten(Kontakt kontakt, String name, String ip, boolean chatBeibehalten) {
		if(chatBeibehalten) {
			kontakt.setContactName(name);
			kontakt.setContactIP(ip);
		}else {
			kontaktEntfernen(kontakt);
			kontaktHinzufuegen(new Kontakt(name, ip, kontakte.size()));
		}
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
	
	public void aktiverKontaktSetzen(Kontakt aktiv) {
		aktiverKontakt = aktiv;
		chatPane.setText("");
		for(Nachricht nachricht : aktiverKontakt.nachrichtenGeben()) {
			neueNachrichtAnzeigen(nachricht.textGeben(), nachricht.istVonMir());
		}
		aktiv.setNewMessage(false);
		this.setTitle("WhatsGluck - " + aktiv.getContactName());
	}
	
	public void schluesselPaarSenden(Kontakt ziel) {
		try {
			BigInteger[] keyPair = RsaEncrypt.getNewKeyPair();
			Socket sendeSocket = new Socket();
			sendeSocket.connect(new InetSocketAddress(ziel.getContactIP(), PORT_NUMBER), 150);
			PrintWriter out = new PrintWriter(sendeSocket.getOutputStream());
			
			
			out.println("//##KeyPair##//" + keyPair[0] + "//" + keyPair[1]);
			
			out.flush();
			sendeSocket.close();
			
			ziel.setPrivateModul(keyPair[0]);
			ziel.setPrivateKey(keyPair[2]);
		} catch (UnknownHostException e) {
			ziel.setOnline(false);
		} catch (IOException e) {
			ziel.setOnline(false);
		}
	}
	
	public void schluesselPaarEmpfangen(String schluesselPaar, Kontakt sender) {
		try {
			String[] key = schluesselPaar.split("//");
			BigInteger modul = new BigInteger(key[0]);
			BigInteger publicKey = new BigInteger(key[1].replaceAll("\n", ""));
			
			BigInteger[] keyPair = RsaEncrypt.getNewKeyPair();
			String keyPairString = keyPair[0].toString() + "//" + keyPair[1].toString();
			Socket senderSocket = new Socket();
			senderSocket.connect(new InetSocketAddress(sender.getContactIP(), PORT_NUMBER), 150);
			PrintWriter out = new PrintWriter(senderSocket.getOutputStream());
			out.println("//%%KeyPair%%//" + keyPairString);
			out.flush();
			
			senderSocket.close();
			
			sender.setPublicModul(modul);
			sender.setPrivateModul(keyPair[0]);
			sender.setPublicKey(publicKey);
			sender.setPrivateKey(keyPair[2]);
			sender.setOnline(true);
		} catch(ArrayIndexOutOfBoundsException e) {
			JOptionPane.showConfirmDialog(this, "Ungültiger Schlüsseltausch von " + sender.getContactName(), "Error", JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void schluesselPaarAkzeptieren(String schluesselPaar, Kontakt sender) {
		try {
			String[] key = schluesselPaar.split("//");
			BigInteger modul = new BigInteger(key[0]);
			BigInteger publicKey = new BigInteger(key[1].replaceAll("\n", ""));
			sender.setPublicModul(modul);
			sender.setPublicKey(publicKey);
			sender.setOnline(true);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void closeWindow() {
		abmelden();
		exportiere();
		WindowEvent wev = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
	    Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);
	    setVisible(false);
	    dispose();
	    System.exit(0); 

	}
	
	public void abmelden() {
		listening = false;
		for(Kontakt k : kontakte) {
			if(k.isOnline()) {
				try {
					Socket sendeSocket = new Socket();
					sendeSocket.connect(new InetSocketAddress(k.getContactIP(), PORT_NUMBER), 1000);
					PrintWriter out = new PrintWriter(sendeSocket.getOutputStream());
					
					out.println("//##Offline##//");
					
					out.flush();
					
					sendeSocket.close();
					
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
