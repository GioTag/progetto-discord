package GUI;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import javax.swing.*;

import AppBusiness.*;
import Dao.*;

public class GuiServer extends JPanel {
	//ATTRIBUTI
	private Server server;
	private Canale canale = null;
	private HashMap<Integer,Canale> listaCanaliBusiness;
	private HashMap<Integer,Utente> listaUtentiBusiness;
	private LinkedList<Messaggio_pubblico> listaMessBusiness;
	private String permessi;
	//Componenti
	
	
	private JPanel canali;
	private JPanel chat;
	private JPanel utenti;
	
	private Dimension maxD = new Dimension(150,30);
	
	private JTextArea chatText;
	private JTextField chatInput;

	private HashSet<JButton> listaCanali;
	private HashSet<JLabel> listaUtenti;
	private JButton createCanale;
	private JButton modera;
	private JLabel chatName;
	private JButton invito;
	
	public GuiServer(Server server) throws SQLException {
		
		this.setMinimumSize(new Dimension(400,300));
		this.server = DbData.serverList.get(server.getId());
		this.permessi = DaoRuolo.getPermessi(DbData.user,this.server);
		System.out.println(permessi);
		listaCanaliBusiness = new HashMap<Integer,Canale>();
		listaUtentiBusiness = new HashMap<Integer,Utente>();
		
		listaCanali = new HashSet<JButton>();
		listaUtenti = new HashSet<JLabel>();
		
		this.setLayout(new BorderLayout());
		chat = new JPanel();
		utenti = new JPanel();
		canali = new JPanel();
	
		canali.setLayout(new BoxLayout(canali,BoxLayout.Y_AXIS));
		canali.setBackground(Color.decode("#27496D"));
		
		utenti.setLayout(new BoxLayout(utenti,BoxLayout.Y_AXIS));
		utenti.setBackground(Color.decode("#27496D"));
		
		chat.setLayout(new BorderLayout());
		chat.setBackground(Color.decode("#00A8CC"));
		
		this.add(canali,BorderLayout.WEST);
		this.add(chat,BorderLayout.CENTER);
		this.add(utenti,BorderLayout.EAST);	
		utenti.setBorder(BorderFactory.createMatteBorder(3,3,3,3,utenti.getBackground()));
		
		createCanali();
		createUserList();
		createChat();
		
		this.setVisible(true);	
		System.out.println(server.getNome());
		this.refreshGui(this.server.getId());
	}
	


	private void createUserList() throws SQLException {		
		utenti.removeAll();
		
		Iterator<Utente> it = server.getUtenti().iterator();
		
		JLabel par = new JLabel("-UTENTI-");
		par.setFont(new Font("Serif",Font.BOLD,25));
		par.setForeground(Color.RED);
		this.utenti.add(par);
		
		while(it.hasNext()) {
			Utente u = it.next();	
			listaUtentiBusiness.put(u.getId(), u);		
			JLabel us = new JLabel(u.getUsername());		
			System.out.println(u.getUsername());
			us.setFont(new Font("Serif",Font.PLAIN,20));
			us.setForeground(Color.WHITE);
			us.setName(u.getUsername());
			
			us.setMinimumSize(maxD);
			us.setMaximumSize(maxD);
			
			this.utenti.add(us);		
		}
		
		invito = new JButton("invita utente");
		invito.setBackground(Color.RED);
		utenti.add(invito);
		
		invito.addActionListener(e -> {
			try {
				new GuiInvito("inviti",this.server);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		
	}
	
	public void createCanali() throws SQLException {
		canali.removeAll();
		//Creazione tasto moderazione
		if(permessi.equals("Admin") || permessi.equals("Moderatore")) {
			System.out.println();
			modera = new JButton("Moderazione");
			modera.setBackground(Color.yellow);
			modera.addActionListener(e -> {
				try {
					apriFinestraModerazione(e);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			});
			modera.setMinimumSize(maxD);
			modera.setMaximumSize(maxD);
			
			canali.add(modera);
		}
		//Creazione canali accessibili
		Iterator<Canale> it = server.getCanali().iterator();
		while(it.hasNext()) {
			Canale c = it.next();	
			listaCanaliBusiness.put(c.getId(),c);
			
			if(this.canale == null)
				this.canale = c;		
			JButton bt = new JButton(c.getNome());
			bt.setName(Integer.toString(c.getId())); 
			bt.addActionListener(e -> {
				try {
					canalePremuto(e);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			});
			bt.setMinimumSize(maxD);
			bt.setMaximumSize(maxD);
			canali.add(bt);
		}
		//crea tasto (new channel) solo se l'utente ha i permessi di moderazione in quel server
		if(permessi.equals("Admin") || permessi.equals("Moderatore")) {
			createCanale = new JButton("  +  ");
			createCanale.setBackground(Color.yellow);
			createCanale.addActionListener(e -> creaCanale(e));
			createCanale.setMinimumSize(maxD);
			createCanale.setMaximumSize(maxD);
			canali.add(createCanale);
		}
		
	}
	
	public void createChat() throws SQLException {
		chat.removeAll();
		
		this.chatName = new JLabel(this.canale.getNome());
		chatName.setFont(new Font("Serif",Font.BOLD,25));
		chatName.setForeground(Color.RED);
		
		chatInput = new JTextField();
		chatInput.addActionListener(e -> {
			try {
				sendMessagge(e);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		chatText=new JTextArea();
		chatText.setBackground(Color.decode("#00A8CC"));
		chatText.setEditable(false);
		
		chat.add(chatInput,BorderLayout.SOUTH);
		chat.add(chatText,BorderLayout.CENTER);
		chat.add(chatName,BorderLayout.NORTH);
		printChat();
	}
	
	
	public void printChat() throws SQLException {
		System.out.println(canale.getId());
		this.chatName.setText(this.canale.getNome());
		this.chatText.setText("");
		
		this.listaMessBusiness = DaoMessaggiPubblici.getMessaggiInCanale(this.canale);
		
		Iterator<Messaggio_pubblico> it = this.listaMessBusiness.iterator();
		
		while(it.hasNext()) {
			Messaggio_pubblico msg = it.next();
			this.chatText.append(msg.get_mittente().getUsername() + ":" + msg.getTesto() + "\n");
		}
	}
	//BUTTON LISTENERS
	private void creaCanale(ActionEvent e) {
		// TODO Auto-generated method stub

	}

	
	public void canalePremuto(ActionEvent e) throws SQLException {
		JButton or = (JButton)e.getSource();
		String nome = or.getName();
		
		this.canale = listaCanaliBusiness.get(Integer.parseInt(nome));		
		printChat();
	}
	
	private void apriFinestraModerazione(ActionEvent e) throws SQLException {
		// TODO Auto-generated method stub
		JFrame f = new JFrame(server.getNome() + " - Moderazione");
		f.add(new GuiModerazione(server));
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		f.setVisible(true);
		f.setMinimumSize(new Dimension(400,400));
	}
	
	private void sendMessagge(ActionEvent e) throws SQLException{ 
		System.out.println("invio premuto");
		
		String testo = this.chatInput.getText();
		if(testo.isEmpty())
				return;
		
		this.chatInput.setText("");
		this.chatText.append(DbData.user.getUsername() + ":" + testo);
		DaoMessaggiPubblici.materializeMessaggio(new Messaggio_pubblico(0, new Timestamp(System.currentTimeMillis()), DbData.user, testo, this.canale.getId()));
		return; 
	}

	
	public void refreshGui(int newServer) throws SQLException {
		System.out.println(this.server.getId());
		System.out.println(newServer);
		
		//if(this.server.getId() == newServer)
			//return;	

		this.server = DbData.serverList.get(newServer);
		this.permessi = DaoRuolo.getPermessi(DbData.user,this.server);
		System.out.println(permessi);
		
		listaCanali.clear();
		listaUtenti.clear();
		createCanali();
		createUserList();
		createChat();
	}
}
