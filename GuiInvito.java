package GUI;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

import AppBusiness.*;
import Dao.*;
import java.util.*;


public class GuiInvito extends JFrame{
	
	private Container c;
	
	private LinkedList<JPanel> containerUtenti;
	private LinkedList<JLabel> nomeUtenti;
	private LinkedList<JButton> invita;
	
	private int w = 600;
	private int h = 600;
	
	//creo hashmap di amici dell'utente loggato che non fanno parte del server attuale
	private HashMap<Integer, Utente> amici;
	
	public GuiInvito(String nome, Server s) throws SQLException {
		super(nome);
		this.setSize(w,h);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setResizable(false);
		this.setVisible(true);
		
		c=this.getContentPane();
		c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
		
		this.amici = DaoAmicizia.getAmiciNonInServer(s);
		System.out.println("AMICI NON IN SERVER");
		for (int key : amici.keySet()) { System.out.println(amici.get(key));} 
		
		//containerUtenti=new LinkedList<JPanel>();
		nomeUtenti=new LinkedList<JLabel>();
		invita=new LinkedList<JButton>();
		
		for (int key : amici.keySet()) {
			Utente u = amici.get(key);
			
			JLabel l = new JLabel(u.getUsername());
			JButton i = new JButton("invita");
			i.setName(Integer.toString(u.getId()));
			JPanel cont = new JPanel();
			
			cont.add(l);
			cont.add(Box.createHorizontalStrut(15));
			cont.add(i);
			cont.add(Box.createHorizontalStrut(15));
			
			nomeUtenti.add(l);
			invita.add(i);
			
			cont.setMinimumSize(new Dimension(40,40));
			cont.setMaximumSize(new Dimension(500,80));
			
			i.addActionListener(e -> {
				try {
						DaoPartecipa.creaInvito(s, u);
						this.refresh();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				catch (NumberFormatException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}		
			});
			c.add(cont);
		}
		
	}

	public void refresh(){
		this.revalidate();
		this.repaint();
	}
	
}
