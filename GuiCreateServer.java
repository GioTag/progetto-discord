package GUI;

import javax.swing.*;
/*
 * Gui usata per prendere i dati in input al momento della creazione di un nuovo server
 */
public class GuiCreateServer extends JPanel {
	private JTextField nameField = new JTextField(8);
	
	public GuiCreateServer() {	    
	    this.add(new JLabel("nome server:"));
	    this.add(nameField);
	    
	}
	
	public String getServerName() {
		return nameField.getText();
	}
}
