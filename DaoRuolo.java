package Dao;
import java.sql.*;
import java.util.LinkedList;

import AppBusiness.*;

public class DaoRuolo {
    public static LinkedList<Ruolo> ruoliInServer (Server server) throws SQLException {
        PreparedStatement stm = DbData.getConnection().prepareStatement("SELECT r.* FROM  Ruoli r "
                													  + "WHERE r.id_server = ? AND r.nome <> 'ADMIN'");
        stm.setInt(1, server.getId());
        ResultSet rs = stm.executeQuery();
        
        LinkedList<Ruolo> r = new LinkedList<Ruolo>();
        
        while(rs.next()) {
        	r.add(new Ruolo(rs.getInt("id"),rs.getString("nome"),rs.getInt("id_creatore"),rs.getInt("id_server"),rs.getDate("data_creazione"),rs.getString("tipo")));
        }
        
        return r;
    }

    public static void ruoliUtente  (String nUtente) throws SQLException {
        PreparedStatement stm = DbData.getConnection().prepareStatement("SELECT r.* FROM Possiede p, Utenti u"
                + "WHERE u.nome=? and p.id_utente=u.id");
        stm.setString(1, nUtente);
    }

    public static String getPermessi(Utente utente,Server server) throws SQLException{
        PreparedStatement stm = DbData.getConnection().prepareStatement("SELECT DISTINCT r.tipo FROM possiede p, ruoli r "
                													  + "WHERE p.id_utente = ? and p.id_ruolo = r.id and r.id_server =?");
        stm.setInt(1, utente.getId());
        stm.setInt(2, server.getId());
        
        ResultSet rs = stm.executeQuery(); 
        LinkedList<String> permessi = new LinkedList<String>();
        while(rs.next()){
        	permessi.add(rs.getString("tipo"));
        }
        if(permessi.contains("Admin")) return "Admin";
        if(permessi.contains("Moderatore")) return "Moderatore";
        return "Base";
    }
    public static void materializeRuolo(Ruolo r) throws SQLException {
    	PreparedStatement stm = DbData.getConnection().prepareStatement("Insert into ruoli (nome,data_creazione,tipo,id_creatore,id_server) VALUES(?,?,?,?,?)");
    	
    	stm.setString(1, r.getNome());
    	stm.setDate(2,r.getData());
    	stm.setString(3, r.getTipo());
    	stm.setInt(4, r.getId_creatore());
    	stm.setInt(5, r.getId_server());
    	
    	stm.executeQuery();
    }
    //torna 1 se esiste il ruolo passata come parametro
    //altrimenti 0
    public static int existsRole(Ruolo r) throws SQLException {
    	PreparedStatement stm = DbData.getConnection().prepareStatement("select * from ruoli where nome=? and id_server = ?");
    	
    	stm.setString(1, r.getNome());
    	stm.setInt(2,r.getId_server());
    	ResultSet rs = stm.executeQuery();

    	if(rs.next()) return 1;
    	else return 0;
    }
    public static void updateRoleType(Ruolo r,String tipo) throws SQLException {
    	PreparedStatement stm = DbData.getConnection().prepareStatement("update ruoli set tipo =? where id = ? ");
    	stm.setString(1, tipo);
    	stm.setInt(2, r.getId());
    	
    	stm.executeUpdate();
    }
}