package GUI;

import javax.swing.*;

public class GuiRegister extends JPanel{
	private JTextField uField = new JTextField(8);
	private JTextField pField = new JTextField(8);
	private JTextField eField = new JTextField(8);
	public GuiRegister() {	    
	    this.add(new JLabel("username:"));
	    this.add(uField);
	    this.add(Box.createHorizontalStrut(15)); // a spacer
	    this.add(new JLabel("password :"));
	    this.add(pField);
	    this.add(Box.createHorizontalStrut(15));
	    this.add(new JLabel("email:"));
	    this.add(eField);
	    
	}
	
	public String getUsername() {
		return uField.getText();
	}
	public String getPassword() {
		return pField.getText();
	}
	public String getEmail() {
		return eField.getText();
	}

}
