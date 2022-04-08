package GUI;
import java.awt.Container;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.sql.SQLException;
import javax.swing.*;
import AppBusiness.*;
import Dao.*;
/*
 * Prima gui dell'applicazione, permette di fare il login e di registrarsi
 */
public class GuiLogin extends JFrame {
	
	private int w = 300;
	private int h = 150;
	
	private Container c;
	private JPanel textArea;
	private JPanel buttons;
	private JLabel userLabel;
	private JLabel passLabel;
	private JTextField userInput;
	private JPasswordField passInput;
	private JButton login;
	private JButton register;
	
	public GuiLogin(String nome) {
		super(nome);
		this.setSize(w,h);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);	
		
		c = this.getContentPane();
		textArea = new JPanel();
		buttons = new JPanel();
		textArea.setLayout(new GridLayout(3,3));
		
		c.add(textArea,BorderLayout.CENTER);
		c.add(buttons,BorderLayout.SOUTH);
		c.add(new JPanel(),BorderLayout.NORTH);
		
		userInput = new JTextField(5);
		passInput = new JPasswordField(5);
		login = new JButton("Login");
		register = new JButton("Register");
		
		userLabel = new JLabel("username");
		passLabel = new JLabel("password");
		
		
		textArea.add(userLabel);
		textArea.add(userInput);
		
		textArea.add(passLabel);
		textArea.add(passInput);
		textArea.add(Box.createHorizontalStrut(1));
		
		buttons.add(login);
		buttons.add(register);
		
		this.setVisible(true);	
		
		login.addActionListener(e -> {
			try {
				loginPressed();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		
		register.addActionListener(e -> registerPressed());
	}
	
	public void loginPressed() throws Exception{
		System.out.println("login premuto");
		String _username = this.userInput.getText();
		char[] _password = this.passInput.getPassword();
		
		try {
			if(DaoUtente.login(_username, _password)) {
				System.out.println("utente loggato");
				Gui window = new Gui("utente-" + _username);
				this.dispose();
				//utente loggato: procediamo nel ottenere tutti i dati relativi a canali,server e ruoli
			}else
				JOptionPane.showMessageDialog(null,"login non riuscito, credenziali invalide", "attenzione",  JOptionPane.ERROR_MESSAGE);
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return;	
	}
	
	public void registerPressed() {
		System.out.println("Register premuto");
		
		GuiRegister gr = new GuiRegister();
		int result = JOptionPane.showConfirmDialog(null, gr,"inserire i valori",JOptionPane.OK_CANCEL_OPTION);
		if(result == 0 && !gr.getUsername().isEmpty() && !gr.getPassword().isEmpty()) {
			try {
				if(!DaoUtente.materializeUtente(new Utente(0,gr.getName(),gr.getPassword(),gr.getEmail()))) //viene passato un id temporaneo
					JOptionPane.showMessageDialog(null, "I dati sono in uso da altri utenti!", "attenzione!", JOptionPane.ERROR_MESSAGE);
				
			}catch (SQLException e) {
				e.printStackTrace();
			}
		}
	
	}
}
