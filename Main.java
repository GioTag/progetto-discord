package Main;
import javax.swing.JOptionPane;
import Dao.DbData;
import GUI.*;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			DbData.initializeConnection();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			new JOptionPane("Connessione non risucita");
		}
		GuiLogin lg = new GuiLogin("LOGIN");
	}
	
}
