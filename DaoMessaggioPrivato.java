package Dao;
import AppBusiness.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedList;

public class DaoMessaggioPrivato {	
    public static LinkedList<Messaggio_privato> getMessaggiTraUtenti(Utente user) throws SQLException {
        PreparedStatement stm = DbData.getConnection().prepareStatement("select m.*\r\n"
        		+ "from MPrivati m\r\n"
        		+ "where (id_utente_mitt = ? and id_utente_dest = ?)OR(id_utente_mitt = ? and id_utente_dest = ?)\r\n"
        		+ "order by Data DESC;");

        stm.setInt(1, DbData.user.getId());
        stm.setInt(2, user.getId());
        stm.setInt(3, user.getId());
        stm.setInt(4, DbData.user.getId());

        ResultSet rs = stm.executeQuery();
        
        LinkedList<Messaggio_privato> list = new LinkedList<Messaggio_privato>();
        
        while(rs.next()) {
        	int id = rs.getInt("id");
        	Timestamp data = rs.getTimestamp("data");
        	int mitt = rs.getInt("id_utente_mitt");
        	int dest = rs.getInt("id_utente_dest");
        	String testo = rs.getString("testo");
        	
        	Messaggio_privato m = new Messaggio_privato(id, data, mitt, dest, testo);
        	
        	list.add(m);
        }
        return list;
    }

	public static void materializeMessaggio(Messaggio_privato m) throws SQLException {
		PreparedStatement stm = DbData.getConnection().prepareStatement("INSERT INTO Mprivati (testo,data,id_utente_mitt ,id_utente_dest) VALUES (?,?,?,?);");
		stm.setString(1, m.getTesto());
		stm.setTimestamp(2,m.getData());
		stm.setInt(3, m.getid_mittente());
		stm.setInt(4, m.getid_destinatario());
		stm.executeUpdate();
	}  
}
