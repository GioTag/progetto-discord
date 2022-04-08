package Dao;
import AppBusiness.*;

import java.util.LinkedList;
import java.sql.*;

public class DaoPossiede {
    public static LinkedList<Utente> getUtentiConRuolo(Ruolo r) throws SQLException{ 
    	//stampa tutti gli utenti che un dato ruolo passato in input
        PreparedStatement stm = DbData.getConnection().prepareStatement("select u.* from utenti u, possiede p, ruoli r "
                													  + "where r.id=? and p.id_ruolo=r.id and p.id_utente=u.id");
        stm.setInt(1, r.getId());

        ResultSet rs = stm.executeQuery();

        LinkedList<Utente> utentiConRuolo = new LinkedList<Utente>();
        while(rs.next()) {
            Utente u = new Utente(rs.getInt("id"),rs.getString("username"),rs.getString("email"),rs.getString("password"));
            System.out.println(u.getUsername());
            utentiConRuolo.add(u);
        }
        return utentiConRuolo;
    }

    public static void materializePossiede (int r, int u) throws SQLException {    //assegna un ruolo a un utente
        PreparedStatement stm = DbData.getConnection().prepareStatement("insert into possiede(id_ruolo, id_utente) values (?,?)");
        stm.setInt(1, r);
        stm.setInt(2, u);

       stm.executeUpdate();
    }
    
    public static void removePossiede(int r, int u) throws SQLException {    //assegna un ruolo a un utente
        PreparedStatement stm = DbData.getConnection().prepareStatement("DELETE FROM possiede WHERE id_ruolo = ? and id_utente = ? ");
        stm.setInt(1, r);
        stm.setInt(2, u);

        stm.executeUpdate();
    }
}