package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.*;

import AppBusiness.*;
import Dao.*;
/*
 * GuiHome contine la home dell'applicazione, da qui è possibile chattare con gli amici, aggiungere nuovi amici, accettare/rifiutare richieste
 */
public class GuiHome extends JPanel{
	//Pannelli base del Frame
	private JPanel amici;
	private JPanel chat;
	private JPanel commandList;
	//dimensioni delle componenti di amici(pannello)
	private Dimension maxD = new Dimension(150,30);
	
	//Componenti per la Chat
	private JTextArea chatText;
	private JTextField chatInput;
	private JLabel chatName;
	
	//Pannelli per accettare/inviare richieste
	private GuiRichiesteInviate panelRichiesteInviate;
	private GuiRichiesteRicevute panelRichiesteRicevute;
	private GuiAggiungiAmico panelCercaAmici;
	private GuiInvitiAServer panelInviti;
	
	//bottoni per navigare tra i panneli definiti sopra
	private JButton cercaAmici;
	private JButton richiesteRicevute;
	private JButton richiesteInviate;
	private JButton invitiRicevuti;
	
	//lista dei bottoni degli amici
	private HashSet<JButton> listaUtenti;
	private HashMap<Integer,Utente> listaUtentiBusiness;
	private LinkedList<Messaggio_privato> listaMessBusiness;
	
	private Utente chatUser = null;//Primo utente tra gli amici
	
	public GuiHome() throws SQLException {
		super();
		createGui();
		//Inizializzazione dei pannelli con relativi LayoutManager		
	}
	
	public void createGui() throws SQLException {
		this.setMinimumSize(new Dimension(400,300));		
		listaUtenti = new HashSet<JButton>();	
		this.setLayout(new BorderLayout());
		
		chat = new JPanel();
		commandList = new JPanel();
		amici = new JPanel();
		
		panelCercaAmici = new GuiAggiungiAmico();
		panelRichiesteInviate = new GuiRichiesteInviate();
		panelRichiesteRicevute = new GuiRichiesteRicevute();
		panelInviti = new GuiInvitiAServer();
		
		amici.setLayout(new BoxLayout(amici,BoxLayout.Y_AXIS));
		amici.setBackground(Color.decode("#27496D"));
		
		chat.setLayout(new BorderLayout());
		chat.setBackground(Color.decode("#00A8CC"));
		
		commandList.setBorder(BorderFactory.createLineBorder(Color.red));
		
		this.add(amici,BorderLayout.WEST);
		this.add(chat,BorderLayout.CENTER);	
		this.add(commandList,BorderLayout.NORTH);
		
		//Metodi di creazioni
		createCommandList();
		createAmici();
		createChat();
		this.setVisible(true);
	}
	/*
	 * Metodi di creazione della Gui
	 */
	//Lista dei comandi mostrata in alto
	private void createCommandList() {
		cercaAmici = new JButton("Aggiungi Amico");
		richiesteRicevute = new JButton("Richieste ricevute");
		richiesteInviate = new JButton("Richieste inviate");
		invitiRicevuti = new JButton("Inviti ricevuti");
		
		this.commandList.add(cercaAmici);
		this.commandList.add(richiesteRicevute);
		this.commandList.add(richiesteInviate);
		this.commandList.add(invitiRicevuti);
		
		cercaAmici.addActionListener(e -> cercaAmici());
		richiesteRicevute.addActionListener(e -> richiesteRicevute());
		richiesteInviate.addActionListener(e -> {
			try {
				richiesteInviate();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		invitiRicevuti.addActionListener(e -> invitiRicevuti());
	}
	
	private void createAmici() throws SQLException{
		this.listaUtentiBusiness = DaoAmicizia.getAmici();	
		for (Integer key : listaUtentiBusiness.keySet()) {
			Utente u = listaUtentiBusiness.get(key);
			if(this.chatUser == null)
				this.chatUser = u;
			
			JButton bt = new JButton(u.getUsername());
			bt.setName(Integer.toString(u.getId()));
			bt.addActionListener(e -> {
				try {
					utentePremuto(e);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			});
			bt.setMinimumSize(maxD);
			bt.setMaximumSize(maxD);
			amici.add(bt);	
		}
		
	}
	public void createChat() throws SQLException {
		chat.removeAll();
		
		this.chatName = new JLabel(this.chatUser.getUsername());
		chatName.setFont(new Font("Serif",Font.BOLD,25));
		chatName.setForeground(Color.RED);
		
		chatInput = new JTextField();
		chatInput.addActionListener(e -> {
			try {
				sendMessagge(e);
			} catch (SQLException e1) {
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
		System.out.println(chatUser.getId());
		this.chatName.setText(this.chatUser.getUsername());
		this.chatText.setText("");
		
		this.listaMessBusiness = DaoMessaggioPrivato.getMessaggiTraUtenti(this.chatUser);
		
		Iterator<Messaggio_privato> it = this.listaMessBusiness.iterator();
		
		while(it.hasNext()) {
			Messaggio_privato msg = it.next();
			if(msg.getid_destinatario()==DbData.user.getId())
				this.chatText.append(chatUser.getUsername() + ":" + msg.getTesto() + "\n");
			else
				this.chatText.append(DbData.user.getUsername() + ":" + msg.getTesto() + "\n");
		}
	}
	//LISTENERS dei bottoni	
	//Tris di bottoni per navigare nei pannelli richieste e cerca
	private void richiesteInviate() throws SQLException {
		BorderLayout l = (BorderLayout)this.getLayout();
		this.remove(l.getLayoutComponent(BorderLayout.CENTER));
		this.add(panelRichiesteInviate=new GuiRichiesteInviate(),BorderLayout.CENTER);
		 refresh();
		return;
	}

	private void richiesteRicevute() {
		BorderLayout l = (BorderLayout)this.getLayout();
		this.remove(l.getLayoutComponent(BorderLayout.CENTER));
		this.add(panelRichiesteRicevute,BorderLayout.CENTER);
		refresh();
		return;
	}

	private void cercaAmici() {
		BorderLayout l = (BorderLayout)this.getLayout();
		this.remove(l.getLayoutComponent(BorderLayout.CENTER));
		this.add(panelCercaAmici,BorderLayout.CENTER);
		refresh();
		return;
	}
	private void invitiRicevuti() {
		BorderLayout l = (BorderLayout)this.getLayout();
		this.remove(l.getLayoutComponent(BorderLayout.CENTER));
		this.add(panelInviti,BorderLayout.CENTER);
		refresh();
		return;
	}
	
	/////////////////////////////////////////////////////////////////
	//ascoltatore che aggiorna la chat mostrata
	/////////////////////////////////////////////////////////////////
	public void utentePremuto(ActionEvent e) throws SQLException {
		BorderLayout l = (BorderLayout)this.getLayout();
		this.remove(l.getLayoutComponent(BorderLayout.CENTER));
		this.add(chat,BorderLayout.CENTER);
		JButton or = (JButton)e.getSource();
		
		String nome = or.getName();
		
		this.chatUser = listaUtentiBusiness.get(Integer.parseInt(nome));		
		printChat();
	}
	//acoltatore che invia i messaggi in chat
	private void sendMessagge(ActionEvent e) throws SQLException{
		//non sono sicuro 
		System.out.println("invio premuto");
		
		String testo = this.chatInput.getText();
		if(testo.isEmpty())
				return;
		this.chatInput.setText("");
		
		this.chatText.append(DbData.user.getUsername() + ":" + testo);
		DaoMessaggioPrivato.materializeMessaggio(new Messaggio_privato(0, new Timestamp(System.currentTimeMillis()), DbData.user.getId(), this.chatUser.getId(),testo));		
	}
	public void refresh(){
		this.revalidate();
		this.repaint();
	}
}
