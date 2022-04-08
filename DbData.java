package Dao;
import java.sql.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import AppBusiness.*;

public class DbData {
	public static final String username="postgres";
	public static final String password="postgres";
	public static final String url="jdbc:postgresql://192.168.136.133/postgres";
	private static Connection conn = null;
	
	//Dati Utente
	public static Utente user = null;
	public static HashMap<Integer,Server> serverList = new HashMap<Integer,Server>();
	
	public static void initializeConnection() throws Exception {
		Class.forName("org.postgresql.Driver");
		conn = DriverManager.getConnection(url, username,password);	
		Statement stm = conn.createStatement();
		stm.executeUpdate("set search_path = 'progetto_esame'");
		stm.close();
	}
	
	public static Connection getConnection() {
		return conn;
	}
	
	public static String getUsername() {
		return user.getUsername();
	}
}
 