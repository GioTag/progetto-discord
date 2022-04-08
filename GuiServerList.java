/*package GUI;
import javax.swing.*;

import AppBusiness.Server;
import Main.DbData;

import java.sql.*;
import java.util.HashSet;
import java.util.LinkedList;

public class GuiServerList extends JPanel {
	private String firstServer = null;
	private HashSet<JButton> servers;
	private String username;
	private Connection conn;
	
	public GuiServerList() {
		servers = new HashSet<JButton>();	
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
	}
	
	public void getServers() throws Exception {
		
		
		while(rs.next()) {
			if(firstServer == null)
				firstServer = rs.getString("nome");
			
			JButton button = new JButton(rs.getString("nome"));
			button.addActionListener(null);		
		}	
	}
}
*/