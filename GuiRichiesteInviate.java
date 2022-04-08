package GUI;

import java.awt.Dimension;
import java.sql.SQLException;
import java.util.*;
import AppBusiness.*;
import Dao.DaoAmicizia;
import javax.swing.*;

public class GuiRichiesteInviate extends JPanel {
	private LinkedList<JPanel> containerRichieste;
	private LinkedList<JLabel> nomeUtenti;
	private LinkedList<JButton> annullaRichiesta;
	
	private HashMap<Integer,Utente> richieste;
	
	public GuiRichiesteInviate() throws SQLException {
		super();
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		createGui();
	}
	
	public void createGui() throws SQLException {
		this.removeAll();
		this.richieste = DaoAmicizia.getRichiesteInviate();
		containerRichieste = new LinkedList<JPanel>();
		nomeUtenti = new LinkedList<JLabel>();
		annullaRichiesta = new LinkedList<JButton>();
		
		for(int key : richieste.keySet()) {
			Utente u = richieste.get(key);
			
			JLabel l = new JLabel(u.getUsername());
			
			JButton a = new JButton("annulla");
			a.setName(Integer.toString(u.getId()));
			
			JPanel container = new JPanel();
			container.add(l);
			container.add(Box.createHorizontalStrut(15));
			container.add(a);
			container.add(Box.createHorizontalStrut(15));
			
			containerRichieste.add(container);
			nomeUtenti.add(l);
			annullaRichiesta.add(a);
			
			container.setMinimumSize(new Dimension(40,40));
			container.setMaximumSize(new Dimension(500,80));
			
			a.addActionListener(e -> {		//al bottone assegno la funzione di annullamento richiesta
				try {
					DaoAmicizia.cancellaRichiestaInviata(Integer.parseInt(((JButton)e.getSource()).getName()));	//passo un intero alla funzione
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

	private void refresh() {
		this.revalidate();
		this.repaint();
	}
	
	
}