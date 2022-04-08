package GUI;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Date;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.*;
import AppBusiness.*;
import Dao.*;

/*========================================================================================================================================
 Finestra usata per moderare un server, permette la creazione di Ruoli, Canali e l'assegnazione dei permessi per ciascun ruolo
==========================================================================================================================================*/
public class GuiModerazione extends JPanel {
	//===========ATTRIBUTI============//
	private final String[] listaPermessiAdmin = {"Admin","Moderatore","Base"};
	private final String[] listaPermessiModeratore = {"Moderatore","Base"};
	private String[] permessi;
	private Server server;
	
	//BorderLayout.NORTH
	private JPanel north;
	private JLabel nomeServer;
	private JButton creaRuolo;
	//BorderLayout.CENTER
	private JTabbedPane finestraCentrale;
	private LinkedList<RolePanel> pannelliRuoli;
	
	public GuiModerazione(Server server) throws SQLException {
		super();
		this.server = server;
		pannelliRuoli = new LinkedList<RolePanel>();
		
		String p = DaoRuolo.getPermessi(DbData.user, server);
		if(p.equals("Admin")) permessi = listaPermessiAdmin;
		else permessi = listaPermessiModeratore;
		
		//bordo
		this.setBorder(BorderFactory.createCompoundBorder());
		this.setLayout(new BorderLayout());
		this.north = new JPanel();
		this.finestraCentrale = new JTabbedPane();
		
		createRolePanel();
		createNorth();
		createCenter();	
	}
	
	private void createRolePanel() throws SQLException {
		Iterator<Ruolo> it = DaoRuolo.ruoliInServer(server).iterator();
		
		while(it.hasNext()) {
			Ruolo r = it.next();
			//controllo se il ruolo puo essere moderato
			boolean admit = false;
			for(int i=0;i<permessi.length;i++) {
				if(permessi[i].equals(r.getTipo())) admit = true;
			}
			if(!admit) continue;
			
			RolePanel role = new RolePanel(r,permessi,server);
			pannelliRuoli.add(role);
		}
	}
	private void createNorth(){
		creaRuolo = new JButton("crea ruolo");
		creaRuolo.addActionListener(e -> {
			try {
				creaRuolo(e);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		nomeServer = new JLabel(server.getNome());
		
		north.setLayout(new GridLayout(1, 3));
		north.add(nomeServer);
		north.add(Box.createHorizontalStrut(70));
		north.add(creaRuolo);	
		
		this.add(north,BorderLayout.NORTH);
	}
	
	private void createCenter() {
		finestraCentrale.removeAll();
		for(RolePanel p: pannelliRuoli) {
			finestraCentrale.addTab(p.getNomeRuolo(),p);
		}
		this.add(finestraCentrale,BorderLayout.CENTER);
		
	}
	
	private void creaRuolo(ActionEvent e) throws SQLException {
		System.out.println("Creazione Ruolo");	
		GuiCreateRole cr = new GuiCreateRole();
		
		int result = JOptionPane.showConfirmDialog(null, cr,"inserire i valori",JOptionPane.OK_CANCEL_OPTION);
		if(result == 0 && !cr.getRoleName().isEmpty()) {
			Ruolo r = new Ruolo(0,cr.getName(),DbData.user.getId(),server.getId(),new Date(System.currentTimeMillis()),"Base");
			if(DaoRuolo.existsRole(r)==0)
				DaoRuolo.materializeRuolo(r);
			else
				JOptionPane.showMessageDialog(null, "Ruolo già esistente", "attenzione!", JOptionPane.ERROR_MESSAGE);
		}
		refreshRoleMenu();
	}
	
	//Usiamo per refreshare la finestra dei ruoli
	public void refreshRoleMenu() throws SQLException{
		createRolePanel();
		createCenter();
	}
}
