package GUI;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.*;
import AppBusiness.*;
import Dao.*;

public class RolePanel extends JPanel {
	//======ATTRIBUTI======//
	private Ruolo ruolo;
	private LinkedList<JCheckBox> inputPermessi;
	private JComboBox tipoRuolo;
	private JPanel cbxContainer;
	private JPanel userCbxContainer;
	private JPanel center;
	private Server s;
	private String[] permessi;
	
	public RolePanel(Ruolo ruolo,String[] permessi,Server s) throws SQLException{
		super();
		this.ruolo = ruolo;
		this.setLayout(new BorderLayout());
		this.s = s;
		this.permessi = permessi;
		
		center = new JPanel();	
		center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
		
		createCanaliCheckBox();
		createUserCheckBox();
		createNorth();
		
		this.add(center,BorderLayout.CENTER);
	}
	
	private void createNorth() {
		//NORTH
		tipoRuolo = new JComboBox<String>(permessi);
		tipoRuolo.setSelectedItem(ruolo.getTipo());
		tipoRuolo.addActionListener(e -> {
			try {
				onSceltaTipo(e);
			} catch (SQLException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		});
		this.add(tipoRuolo,BorderLayout.NORTH);
	}
	private void createUserCheckBox() throws SQLException {
		userCbxContainer = new JPanel();
		userCbxContainer.add(new JLabel("Utenti"));
		LinkedList<Utente> userWithRole = DaoPossiede.getUtentiConRuolo(ruolo);
		
		for(Utente u : s.getUtenti()) {
			JCheckBox ck = new JCheckBox(u.getUsername());
			if(utentiContenuti(userWithRole,u)) ck.setSelected(true);
			else ck.setSelected(false);
			
			ck.setName(Integer.toString(u.getId()));
			ck.addActionListener(e -> {
				try {
					userCheckBoxClicked(e);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			});
			userCbxContainer.add(ck);
		}
		
		center.add(userCbxContainer);
	}

	private void createCanaliCheckBox() throws SQLException {
		//CENTER
		//Prendiamo una lista di canali acceduti dal ruolo in questione
		LinkedList<Canale> _c = DaoAccede.getCanaliAcceduti(ruolo);	
		cbxContainer = new JPanel();
		cbxContainer.add(new JLabel("Canali moderati"));
		
		inputPermessi = new LinkedList<JCheckBox>();
		try {
			for(Canale c : DaoCanali.getCanali(s)) {
				JCheckBox ck = new JCheckBox(c.getNome());
				//se il ruolo accede al canale spuntiamo la checkbox
				if(canaliContenuti(_c,c)) ck.setSelected(true);
				else ck.setSelected(false);
				
				ck.setName(Integer.toString(c.getId()));
				ck.addActionListener(e -> {
					try {
						checkBoxClicked(e);
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				});
				cbxContainer.add(ck);
				inputPermessi.add(ck);	
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		center.add(cbxContainer);
	}
	
	private void onSceltaTipo(ActionEvent e) throws SQLException {
		JComboBox<String> source = (JComboBox<String>)e.getSource();
		String nuovoTipo = (String)source.getSelectedItem();
		
		DaoRuolo.updateRoleType(this.ruolo,nuovoTipo);
		
	}
	private void userCheckBoxClicked(ActionEvent e) throws SQLException {
		JCheckBox cx = (JCheckBox)e.getSource();
		if(cx.isSelected()) DaoPossiede.materializePossiede(ruolo.getId(), Integer.parseInt(cx.getName()));
		else DaoPossiede.removePossiede(ruolo.getId(), Integer.parseInt(cx.getName()));
	}
	
	private void checkBoxClicked(ActionEvent e) throws SQLException {
		JCheckBox cx = (JCheckBox)e.getSource();
		if(cx.isSelected()) DaoAccede.materializeAccede(ruolo.getId(), Integer.parseInt(cx.getName()));
		else DaoAccede.removeAccede(ruolo.getId(), Integer.parseInt(cx.getName()));
	}
	
	public String getNomeRuolo(){
		return ruolo.getNome();
	}
	
	private boolean canaliContenuti(LinkedList<Canale> canali,Canale c) {
		Iterator<Canale> it = canali.iterator();
		
		while(it.hasNext()) {
			if(it.next().getId() == c.getId()) return true;
		}
		return false;
	}
	
	private boolean utentiContenuti(LinkedList<Utente> utenti,Utente u) {
		Iterator<Utente> it = utenti.iterator();
		
		while(it.hasNext()) {
			if(it.next().getId() == u.getId()) return true;
		}
		return false;
	}
}
