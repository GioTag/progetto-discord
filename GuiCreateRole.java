package GUI;

import javax.swing.*;
/*
 * GUI usata per prendere i dati in input al momento della creazione di un nuovo ruolo
 */
public class GuiCreateRole extends JPanel{
	private JTextField roleName = new JTextField(8);

	public GuiCreateRole() {	    
	    this.add(new JLabel("nome:"));
	    this.add(roleName);    
	}
	
	public String getRoleName() {
		return roleName.getText();
	}

}
