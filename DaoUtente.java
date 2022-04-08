package Dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import AppBusiness.Utente;

public class DaoUtente {
	private DaoUtente() {};	
	
	public static boolean login(String name, char[] password) throws SQLException{
		PreparedStatement stm = DbData.getConnection().prepareStatement("SELECT * FROM utenti WHERE username=? AND password=?");
		stm.setString(1,name);
		stm.setString(2, String.valueOf(password));
		
		ResultSet rs = stm.executeQuery();
		
		if(rs.next()) {
			Utente user = new Utente(rs.getInt("id"),rs.getString("username"),rs.getString("email"),rs.getString("password"));
			stm.close();
			
			DbData.user = user;
			return true;
		}
		else {
			stm.close();
			return false;
		}
	}
	
	public static boolean materializeUtente(Utente u) throws SQLException{
		PreparedStatement stm = DbData.getConnection().prepareStatement("SELECT username FROM utenti WHERE username=? OR password=?");
		stm.setString(1,u.getUsername());
		stm.setString(2, u.getPassword());
		
		ResultSet rs = stm.executeQuery();
		
		if(rs.next()) {
			return false;
		}
		
		stm = DbData.getConnection().prepareStatement("INSERT INTO utenti(username,password,email) VALUES(?,?,?,?)");
		
		stm.setString(1,u.getUsername());
		stm.setString(2,u.getPassword());
		stm.setString(3,u.getEmail());
		stm.executeUpdate();
		
		stm.close();
		
		return true;
	}
	
	public static Utente getUtente(int id) throws SQLException{
		Utente u;
		PreparedStatement stm = DbData.getConnection().prepareStatement("SELECT * FROM utenti WHERE id=?;");
		stm.setInt(1,id);
		
		ResultSet rs = stm.executeQuery();
		
		rs.next();
		u= new Utente(rs.getInt("id"),rs.getString("username"),rs.getString("email"),rs.getString("password"));
		System.out.println(u.getUsername());
		return u;
	}
	
	public static LinkedList<Utente> utentiLike(String username) throws SQLException {
        PreparedStatement stm = DbData.getConnection().prepareStatement("select u.* from utenti u where username like ? and u.id <> ?");
        stm.setString(1, username + "%");
        stm.setInt(2, DbData.user.getId());
        ResultSet rs = stm.executeQuery();
        LinkedList<Utente> utentiLike = new LinkedList<Utente>();
        while(rs.next()) {
        	System.out.println("dsfaaaasdsd");
            Utente u = new Utente(rs.getInt("id"),rs.getString("username"),rs.getString("email"),rs.getString("password"));
            utentiLike.add(u);
        }
        return utentiLike;
    }
}
