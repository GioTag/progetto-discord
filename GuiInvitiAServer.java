package GUI;

import java.awt.Dimension;
import java.sql.SQLException;
import java.util.*;
import AppBusiness.*;
import Dao.DaoAmicizia;
import Dao.DaoPartecipa;

import javax.swing.*;

public class GuiInvitiAServer extends JPanel {
	private LinkedList<JPanel> containerInviti;
	private LinkedList<JLabel> nomeServer;
	private LinkedList<JButton> accettaInviti;
	private LinkedList<JButton> rifiutaInviti;
	
	private HashMap<Integer, Server> invito;
	
	public GuiInvitiAServer() throws SQLException{
		super();
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		createGui();
	}
	
	public void createGui() throws SQLException {
		this.removeAll();
		this.invito = DaoPartecipa.getInvitiServer();
		containerInviti = new LinkedList<JPanel>();
		nomeServer = new LinkedList<JLabel>();
		accettaInviti = new LinkedList<JButton>();
		rifiutaInviti = new LinkedList<JButton>();
		
		for(int key : invito.keySet()) {
			Server s = invito.get(key);
			JLabel l = new JLabel(s.getNome());
			JButton a = new JButton("accetta");
			a.setName(Integer.toString(s.getId()));
			JButton r = new JButton("rifiuta");
			r.setName(Integer.toString(s.getId()));
			JPanel container = new JPanel();
			
			container.add(l);
			container.add(Box.createHorizontalStrut(15));
			container.add(a);
			container.add(Box.createHorizontalStrut(15));
			container.add(r);
			
			containerInviti.add(container);
			nomeServer.add(l);
			accettaInviti.add(a);
			rifiutaInviti.add(r);
			
			container.setMinimumSize(new Dimension(40,40));
			container.setMaximumSize(new Dimension(500,80));
			
			a.addActionListener(e -> {
				try {
					DaoPartecipa.accettaInvito(invito.get((Integer.parseInt(((JButton)e.getSource()).getName()))));
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
			
			r.addActionListener(e -> {
				try {
					DaoPartecipa.rifiutaInvito(invito.get((Integer.parseInt(((JButton)e.getSource()).getName()))));
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
