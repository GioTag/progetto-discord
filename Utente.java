package AppBusiness;

import java.sql.SQLException;

import Dao.DbData;

public class Utente {
	private int id;
	private String username;
	private String email;
	private String password;
	
	public Utente(int id, String username,String email,String password)
	{
		this.id=id;
		this.username = username;
		this.email = email;
		this.password = password;
	}
	
	public int getId() { return id; }
	public String getUsername() { return username; }
	public String getEmail() { return email; }
	public String getPassword() { return password; }
}
