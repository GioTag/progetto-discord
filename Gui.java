package GUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.util.HashSet;
import java.util.Iterator;

import javax.swing.*;

import AppBusiness.Server;
import Dao.DaoPartecipa;
import Dao.DbData;
/*
 * Classe gui che crea il frame contente la quasi totalita di panel definiti nel package GUI;
 * Crea una finestra di base che ospita la guiServer o la guiHome
 * 
 */
public final class Gui extends JFrame  {
	
	private final Dimension d = new Dimension(90,30);//dimensioni degli elementi
	private Server firstServer= null;
	
	//dimensioni base del frame
	private int w = 1000;
	private int h = 800;
	
	private Container c;
	//i pannelli delle due gui contenute
	private GuiHome homeGui;
	private GuiServer server;
	//Bottoni crea server e tasto home
	private JButton creaS;
	private JButton home;
	//flag dei pannelli, se 1 guiServer è attivo, altrimenti lo è guiHome
	private boolean serverDisplayed;
	
	//Attributi per creare la lista di server
	private JPanel sList;
	
	public Gui(String nome) throws Exception {
		super(nome);
		DaoPartecipa.getServerList();
		this.setSize(w,h);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);	
		
		c = this.getContentPane();
		c.setLayout(new BorderLayout());
		
	    homeGui = new GuiHome();
	
		createServerList();
		server = new GuiServer(firstServer);
		
		sList.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.red));
		c.add(sList,BorderLayout.WEST);
		c.add(server,BorderLayout.CENTER);
		sList.setBackground(Color.decode("#142850"));
		
		serverDisplayed = true;
		this.setVisible(true);			
	}

	//metodo che crea la lista di server da mostrare sulla collonna di sinistra
	private void createServerList() throws Exception {
	    //ottengo la lista di server dal db
		Server server;
		sList = new JPanel();
		sList.setPreferredSize(d);
		sList.setLayout(new	BoxLayout(sList, BoxLayout.Y_AXIS));
		
		//creazione tasto home
		home = new JButton("home");
		home.setName("home ");
		home.setBackground(Color.orange);
		home.addActionListener(e -> {
			try {
				openHome();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		home.setMaximumSize(d);
		sList.add(home);
		
		//creazione bottoni per i server 
		for(int key : DbData.serverList.keySet()) {
			server = DbData.serverList.get(key);
			
			System.out.println(server.getNome());
			if(firstServer == null)
					this.firstServer = new Server(server.getNome(),server.getData_creazione(),server.getId_creatore(),server.getId());
			
			JButton b = new JButton(server.getNome());
			b.setName(Integer.toString(server.getId()));
			
			b.setMaximumSize(d);
			b.addActionListener(e -> {
				try {
					serverButtonPressed(e);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			});
			
			sList.add(b);	
		}
		
		//tasto crea server
		creaS = new JButton("    +    ");
		creaS.addActionListener(e -> createServer());
		creaS.setMinimumSize(d);
		creaS.setMaximumSize(d);
		sList.add(creaS);
	}
	
	
	//Listeners	dei bottoni
	
	
	//ascoltatore bottoni server
	public void serverButtonPressed(ActionEvent e) throws SQLException{
		if(!serverDisplayed) openHome();
		JButton b = (JButton)e.getSource();
		System.out.println("tasto premuto, server: " + b.getName());
		try {
			server.refreshGui(Integer.parseInt(b.getName()));		
		}catch(SQLException ex) {
			ex.printStackTrace();
		}
		this.setMinimumSize(this.getSize());
		this.pack();
		this.setMinimumSize(null);
	}
	
	//ascoltatore tasto home
	public void openHome() throws SQLException{
		if(serverDisplayed) {
			c.remove(server);
			c.add(homeGui,BorderLayout.CENTER);
			serverDisplayed=false;
			this.setMinimumSize(this.getSize());
			this.pack();
			this.setMinimumSize(null);
		}else {
			c.remove(homeGui);
			c.add(this.server,BorderLayout.CENTER);
			serverDisplayed = true;	
			this.setMinimumSize(this.getSize());
			this.pack();
			this.server.refreshGui(firstServer.getId());
			this.setMinimumSize(null);
		}
	}
	
	//ascoltatore tasto crea server
	public void createServer() {
		GuiCreateServer gr = new GuiCreateServer();
		int result = JOptionPane.showConfirmDialog(null, gr,"inserire dati server",JOptionPane.OK_CANCEL_OPTION);
		
	}	
}
