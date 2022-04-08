package Dao;

import java.sql.*;
import java.util.HashMap;
import AppBusiness.*;

public class DaoPartecipa {
	public static void getServerList() throws SQLException {
		PreparedStatement stm = DbData.getConnection()
		.prepareStatement("SELECT s.* "
						+ "FROM server s,partecipa p "
						+ "WHERE s.id = p.id_server and p.id_utente = ? and p.flag_tipo=1 ;");
		
		HashMap<Integer,Server> list = new HashMap<Integer,Server>();
		
		stm.setInt(1, DbData.user.getId());
		ResultSet rs = stm.executeQuery();
		
		while(rs.next()) {
			String nome = rs.getString("nome");
			Date data_creazione = rs.getDate("data_creazione");
			int id = rs.getInt("id");
			int id_creatore = rs.getInt("id_creatore");
			Server s = new Server(nome,data_creazione,id_creatore,id);
	
			System.out.println("DaoPartecipa, otteniamo i server dell'utente: " +s.getNome());
			
			DaoCanali.getCanaliAccessibili(s);
			getUserInServer(s);
			
			list.put(s.getId(),s);
		}
		DbData.serverList = list;
		stm.close();
		
	}
	public static HashMap<Integer, Server> getInvitiServer() throws SQLException {
		PreparedStatement stm = DbData.getConnection()
		.prepareStatement("SELECT s.* "
						+ "FROM server s,partecipa p "
						+ "WHERE s.id = p.id_server and p.id_utente = ? and p.flag_tipo=0 ;");
		
		HashMap<Integer,Server> inviti = new HashMap<Integer,Server>();
		
		stm.setInt(1, DbData.user.getId());
		ResultSet rs = stm.executeQuery();
		
		while(rs.next()) {
			String nome = rs.getString("nome");
			Date data_creazione = rs.getDate("data_creazione");
			int id = rs.getInt("id");
			int id_creatore = rs.getInt("id_creatore");
			Server s = new Server(nome,data_creazione,id_creatore,id);
	
			System.out.println("DaoPartecipa, otteniamo i server dell'utente: " +s.getNome());
			
			DaoCanali.getCanaliAccessibili(s);
			getUserInServer(s);
			
			inviti.put(s.getId(),s);
		}
		return inviti;
		
	}
	
	
	
	
	public static void getUserInServer(Server s) throws SQLException {
		PreparedStatement stm = DbData.getConnection().prepareStatement("select u.* "
				                                                      + "from utenti u,partecipa p "
				                                                      + "where u.id = p.id_utente and p.id_server = ?;");
		
		stm.setInt(1, s.getId());
		
		ResultSet rs  = stm.executeQuery();
		
		while(rs.next()) {
			Utente u = new Utente(rs.getInt("id"),rs.getString("username"),rs.getString("email"),rs.getString("password"));	
			s.addUtente(u);
			System.out.println("DaoPartecipa : " + u.getUsername());
		}
	}
	
	public static void creaInvito (Server s, Utente u) throws SQLException {    
		//inserisce in "partecipa" la tupla id_server, id_utente, flag_tipo=0
        PreparedStatement stm = DbData.getConnection().prepareStatement("insert into partecipa (id_utente, id_server, data_join, flag_tipo) values (?,?,?,0) ");

        Date data = new Date(System.currentTimeMillis());
        stm.setInt(1, u.getId());
        stm.setInt(2, s.getId());
        stm.setDate(3, data);
        stm.executeUpdate();
    }

    public static boolean accettaInvito (Server s) throws SQLException {    //cambia flag da 0 a 1 se l'utente è stato invitato in precedenza da quel server (c'è una tupla con flag a 0)
        //mette il flag a 1 e cambia la data in quella attuale
        PreparedStatement stm1 = DbData.getConnection().prepareStatement("update partecipa p set flag_tipo=1, data_join=? where p.id_utente=? and p.id_server=? ");

        Date data = new Date(System.currentTimeMillis());
        stm1.setDate(1, data);
        stm1.setInt(2, DbData.user.getId());
        stm1.setInt(3, s.getId());

        stm1.executeUpdate();
        return true;
    }
    public static boolean rifiutaInvito (Server s) throws SQLException {    //cambia flag da 0 a 1 se l'utente è stato invitato in precedenza da quel server (c'è una tupla con flag a 0)
        //mette il flag a 1 e cambia la data in quella attuale
        PreparedStatement stm1 = DbData.getConnection().prepareStatement("delete from partecipa where id_utente=? and id_server=? ");

        Date data = new Date(System.currentTimeMillis());
        stm1.setDate(1, data);
        stm1.setInt(2, DbData.user.getId());
        stm1.setInt(3, s.getId());

        stm1.executeUpdate();
        return true;
    }
    
    
}
