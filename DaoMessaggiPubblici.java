package Dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import AppBusiness.Canale;
import AppBusiness.Messaggio_pubblico;

public class DaoMessaggiPubblici {
	
	public static LinkedList<Messaggio_pubblico> getMessaggiInCanale(Canale c) throws SQLException {
		
		LinkedList<Messaggio_pubblico> list = new LinkedList<Messaggio_pubblico>();
		PreparedStatement stm = DbData.getConnection().prepareStatement("SELECT * from MPubblici WHERE id_canale = ? ORDER by data ASC;");
		
		stm.setInt(1, c.getId());
		ResultSet rs = stm.executeQuery();
		
		while(rs.next()) {
			list.add(new Messaggio_pubblico(rs.getInt("id"), rs.getTimestamp("data"), DaoUtente.getUtente(rs.getInt("id_utente")), rs.getString("testo"), rs.getInt("id_canale")));
		}
		
		return list;
	}

	public static void materializeMessaggio(Messaggio_pubblico m) throws SQLException {
		PreparedStatement stm = DbData.getConnection().prepareStatement("INSERT INTO Mpubblici (testo,data,id_utente,id_canale) VALUES (?,?,?,?);");
		stm.setString(1, m.getTesto());
		stm.setTimestamp(2,m.getData());
		stm.setInt(3, m.get_mittente().getId());
		stm.setInt(4, m.getid_canale());
		stm.executeUpdate();
	}
}
