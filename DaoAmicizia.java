package Dao;

import AppBusiness.*;
import java.util.*;
import java.sql.*;



public class DaoAmicizia {
    public static HashMap<Integer,Utente> getAmici() throws SQLException {
        PreparedStatement stm = DbData.getConnection().prepareStatement("select u.*\r\n"
        																+ "from Utenti u,Amicizia a\r\n"
        																+ "where a.flag_tipo=1 \r\n"
        																+ "	  and((a.id_utente_1 =? and a.id_utente_2 = u.id)\r\n"
        																+ "	  OR(a.id_utente_1 = u.id and a.id_utente_2 = ?));");
        stm.setInt(1, DbData.user.getId());
        stm.setInt(2, DbData.user.getId());
        
        ResultSet rs = stm.executeQuery();
        HashMap<Integer,Utente> amici = new HashMap<Integer,Utente>();
        
        while(rs.next()) {
             Utente u= new Utente (rs.getInt("id"), rs.getString("username"), rs.getString("email"), rs.getString("password"));
             amici.put(u.getId(),u);
        }
        return amici;
    }
    
    //amici in server
    public static HashMap<Integer,Utente> getAmiciNonInServer(Server s) throws SQLException {
        PreparedStatement stm = DbData.getConnection().prepareStatement("select distinct u.* "
        		+ "from Utenti u,Amicizia a, server s "
        		+ "where a.flag_tipo=1 and((a.id_utente_1 = ? and a.id_utente_2 = u.id) OR(a.id_utente_1 = u.id and a.id_utente_2 = ?)) "
        		+ "and not exists (select * from partecipa p where p.id_server = ? and p.id_utente=u.id) ");
        stm.setInt(1, DbData.user.getId());
        stm.setInt(2, DbData.user.getId());
        stm.setInt(3, s.getId());
        
        ResultSet rs = stm.executeQuery();
        HashMap<Integer,Utente> amici = new HashMap<Integer,Utente>();
        
        while(rs.next()) {
             Utente u= new Utente (rs.getInt("id"), rs.getString("username"), rs.getString("email"), rs.getString("password"));
             amici.put(u.getId(),u);
        }
        return amici;
    }
    
    public static void materializeRichiesta (int user) throws SQLException {	
    	//crea una richiesta di amicizia tra due utenti dato il loro id
        //verifica se sono gia amici o se c'è già una richiesta in corso
    	
    	PreparedStatement stm = DbData.getConnection().prepareStatement("select * from utenti u1, utenti u2, amicizia a "
        															  + "where u1.id=? and u2.id=? and ((a.id_utente_1=u1.id and a.id_utente_2=u2.id) or (a.id_utente_1=u2.id and a.id_utente_2=u1.id)) ");	//controllo se stanno nella tabella 'amicizia' (flag 0 o 1)
        stm.setInt(1, DbData.user.getId());
        stm.setInt(2, user);
        ResultSet rs = stm.executeQuery();
        
        if(rs.next()) return; //se rs NON è vuoto significa che i due utenti sono gia amici o hanno gia una richiesta di amicizia in corso
        
        stm = DbData.getConnection().prepareStatement("insert into Amicizia (id_utente_1, id_utente_2, flag_tipo) "	//aggiungo i due id nella tabella 'amicizia' con flag a 0
                + "values (?,?,0)");
        stm.setInt(1, DbData.user.getId());
        stm.setInt(2, user);
        stm.executeUpdate();
        
        stm.close();
        return;
    }
    
    public static void accettaRichiesta (int user) throws SQLException {		//controllo se c'è una richiesta di amicizia in corso tra due utenti e la accetta 
     
        //se esiste la richiesta cambia il flag a 1
        PreparedStatement stm=DbData.getConnection().prepareStatement("update amicizia set flag_tipo=1 where id_utente_1 = ? and id_utente_2 = ?");
        stm.setInt(1, user);
        stm.setInt(2, DbData.user.getId());
        
        stm.executeUpdate();
        return;
    }
    
    public static void cancellaAmicizia(int user) throws SQLException {
    	PreparedStatement stm = DbData.getConnection().prepareStatement("delete from amicizia "
    													   			  + "where id_utente_1 = ? and id_utente_2 =? or id_utente_1 = ? and id_utente_2 = ?");
    	
    	stm.setInt(1, DbData.user.getId());
    	stm.setInt(2, user);
    	stm.setInt(3, user);
    	stm.setInt(4, DbData.user.getId());
    	
    	stm.executeUpdate();
    }
    
    public static void rifiutaRichiestaAmicizia(int user) throws SQLException {
    	PreparedStatement stm = DbData.getConnection().prepareStatement("delete from amicizia "
    																  + "where id_utente_1=? and id_utente_2=?");
    	
    	stm.setInt(1, user);
    	stm.setInt(2, DbData.user.getId());
    	stm.executeUpdate();
    }
    
    public static HashMap<Integer,Utente> getRichiesteRicevute() throws SQLException {
    	PreparedStatement stm = DbData.getConnection().prepareStatement("select u.* from utenti u,amicizia a "
    																  + "where a.id_utente_2 = ? and a.flag_tipo = 0 and a.id_utente_1 = u.id");
    	stm.setInt(1, DbData.user.getId());
    	ResultSet rs= stm.executeQuery();
    	
    	HashMap<Integer,Utente> amici = new HashMap<Integer,Utente>();
    	while(rs.next()) {
            Utente u= new Utente (rs.getInt("id"), rs.getString("username"), rs.getString("email"), rs.getString("password"));
            amici.put(u.getId(),u);
       }
    	return amici;
    }
    public static HashMap<Integer,Utente> getRichiesteInviate() throws SQLException {
    	PreparedStatement stm = DbData.getConnection().prepareStatement("select u.* from utenti u,amicizia a "
    																  + "where a.id_utente_1 = ? and a.flag_tipo = 0 and a.id_utente_2 = u.id");
    	
    	stm.setInt(1, DbData.user.getId());
    	ResultSet rs= stm.executeQuery();

    	HashMap<Integer,Utente> amici = new HashMap<Integer,Utente>();
    	while(rs.next()) {
    		Utente u= new Utente (rs.getInt("id"), rs.getString("username"), rs.getString("email"), rs.getString("password"));
    		amici.put(u.getId(),u);
    	}
    	return amici;
    }
    public static void cancellaRichiestaInviata(int u) throws SQLException {
    	PreparedStatement stm = DbData.getConnection().prepareStatement("DELETE FROM amicizia where id_utente_1 = ? and id_utente_2 = ? and flag_tipo = 0 ");
    	stm.setInt(1, DbData.user.getId());
    	stm.setInt(2, u);
    	stm.executeUpdate();
    }
}