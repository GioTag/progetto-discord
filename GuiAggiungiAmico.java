package GUI;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import AppBusiness.Utente;
import Dao.DaoAmicizia;
import Dao.DaoUtente;

public class GuiAggiungiAmico extends JPanel{
	//componenti finestra cerca amici
	private JTextField ricerca;
	private LinkedList<JLabel> risultatoRicerca;
	private LinkedList<JPanel> containerUtente;
	private LinkedList<JButton> inviaAmicizia;
	private JPanel containerRicerca;
	
	public GuiAggiungiAmico() {
		super();
		this.setLayout(new BorderLayout());
		createPanelloRicerca();
	}
	private void createPanelloRicerca() {
		// TODO Auto-generated method stub
		containerRicerca = new JPanel();
		containerRicerca.setVisible(true);
		this.add(containerRicerca,BorderLayout.CENTER);
		
		containerRicerca.setLayout(new BoxLayout(containerRicerca, BoxLayout.Y_AXIS));

		ricerca = new JTextField();
		ricerca.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
            	JTextField field = (JTextField)e.getSource();
            	if(field.getText().length() == 0) {
            		containerRicerca.removeAll();
            		refresh();
            		return;
            	}
            	try {
					cercaAmici(field.getText());
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
            }
        });
		this.add(ricerca,BorderLayout.NORTH);
	}
	
	public void cercaAmici(String username) throws SQLException {
		LinkedList<Utente>  users = DaoUtente.utentiLike(username);
		containerRicerca.removeAll();
		containerUtente = new LinkedList<JPanel>();
		risultatoRicerca = new LinkedList<JLabel>();
		inviaAmicizia = new LinkedList<JButton>();
		
		for(Utente u : users){
			if(isFriend(u) || requestSent(u)) continue;
			
			JPanel p = new JPanel();
			JLabel l = new JLabel(u.getUsername());
			
			JButton richiesta = new JButton("INVIA RICHIESTA");
			richiesta.setName(Integer.toString(u.getId()));
			richiesta.addActionListener(e -> {
				try {
					DaoAmicizia.materializeRichiesta(Integer.parseInt(((JButton)e.getSource()).getName()));
					this.cercaAmici(username);
				} catch (NumberFormatException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				risultatoRicerca.add(l);
				inviaAmicizia.add(richiesta);
			});
			p.add(l);
			p.add(Box.createHorizontalStrut(20));
			p.add(richiesta);
			p.setBorder(BorderFactory.createEtchedBorder());
			p.setVisible(true);
			containerUtente.add(p);
			containerRicerca.add(p);
		}
		refresh();
	}
	private boolean requestSent(Utente u) throws SQLException {
		HashMap<Integer,Utente> map = DaoAmicizia.getRichiesteInviate();
		for(int key : map.keySet()) {
			Utente us = map.get(key);
			if(us.getId() == u.getId()) return true;
		}
		return false;
	}
	
	private boolean isFriend(Utente u) throws SQLException {
		HashMap<Integer,Utente> map = DaoAmicizia.getAmici();
		for(int key : map.keySet()) {
			Utente us = map.get(key);
			if(us.getId() == u.getId()) return true;
		}
		return false;
	}
	
	public void refresh(){
		this.revalidate();
		this.repaint();
	}
}
