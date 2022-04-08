package GUI;

import java.awt.Dimension;
import java.sql.SQLException;
import java.util.*;
import AppBusiness.*;
import Dao.DaoAmicizia;

import javax.swing.*;

public class GuiRichiesteRicevute extends JPanel {
	private LinkedList<JPanel> containerRichieste;
	private LinkedList<JLabel> nomeUtenti;
	private LinkedList<JButton> accettaRichieste;
	private LinkedList<JButton> rifiutaRichieste;
	
	//LOGIC
	private HashMap<Integer,Utente> richieste;
	
	public GuiRichiesteRicevute() throws SQLException {
		super();
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		createGui();
	}
	public void createGui() throws SQLException {
		this.removeAll();
		this.richieste = DaoAmicizia.getRichiesteRicevute();
		containerRichieste = new LinkedList<JPanel>();
		nomeUtenti = new LinkedList<JLabel>();
		accettaRichieste = new LinkedList<JButton>();
		rifiutaRichieste = new LinkedList<JButton>();
		
		for(int key : richieste.keySet()) {
			Utente u = richieste.get(key);
			//creazione delle componetni
			JLabel l = new JLabel(u.getUsername());
			JButton a = new JButton("accetta");
			a.setName(Integer.toString(u.getId()));
			JButton r = new JButton("rifiuta");
			r.setName(Integer.toString(u.getId()));
			JPanel container = new JPanel();
			
			container.add(l);
			container.add(Box.createHorizontalStrut(15));
			container.add(a);
			container.add(Box.createHorizontalStrut(15));
			container.add(r);
			
			containerRichieste.add(container);
			nomeUtenti.add(l);
			accettaRichieste.add(a);
			rifiutaRichieste.add(r);
			
			container.setMinimumSize(new Dimension(40,40));
			container.setMaximumSize(new Dimension(500,80));
			//logic
			r.addActionListener(e->{
				try {
					DaoAmicizia.rifiutaRichiestaAmicizia(Integer.parseInt(((JButton)e.getSource()).getName()));
					this.createGui();
					refresh();
				} catch (NumberFormatException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			});
			a.addActionListener(e->{
				try {
					DaoAmicizia.accettaRichiesta(Integer.parseInt(((JButton)e.getSource()).getName()));
					this.createGui();
					refresh();
				} catch (NumberFormatException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			});
			
			this.add(container);
		}
	}
	public void refresh(){
		this.revalidate();
		this.repaint();
	}
}
