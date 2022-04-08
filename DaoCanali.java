package Dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import AppBusiness.Canale;
import AppBusiness.Server;

public class DaoCanali {
	
	public static void getCanaliAccessibili(Server s) throws SQLException{
		
		PreparedStatement stm = DbData.getConnection()
		.prepareStatement("select Distinct c.*\r\n"
				+ "from Canali c,Possiede p,Server s,Ruoli r,Accede a,Utenti u,Partecipa pp\r\n"
				+ "where (s.id = ? and u.id =? and c.id_server = s.id and u.id=pp.id_utente\r\n"
				+ "	  and pp.id_server = s.id)\r\n"
				+ "	  AND((p.id_utente = u .id and p.id_ruolo = r.id \r\n"
				+ "	  and r.id = a.id_ruolo and a.id_canale = c.id)\r\n"
				+ "	  OR (u.id =p.id_utente and p.id_ruolo = r.id\r\n"
				+ "		 and r.tipo='Admin' and r.id_server = s.id)\r\n"
				+ "	  OR (NOT EXISTS(select *\r\n"
				+ "				   	from accede ac,Ruoli rr\r\n"
				+ "				   	where ac.id_canale = c.id and ac.id_ruolo = rr.id))\r\n"
				+ ");");
		
		stm.setInt(2,DbData.user.getId());
		stm.setInt(1, s.getId());
		
		ResultSet rs = stm.executeQuery();
		
		while(rs.next()) {
			Canale c = new Canale(rs.getInt("id"),rs.getString("nome"),rs.getInt("id_server"),rs.getInt("id_creatore"),rs.getDate("data_creazione"));
			s.addCanale(c);
			System.out.println("DaoCanali : " + c.getNome());
		}
	}
	
	public static LinkedList<Canale> getCanali(Server s) throws SQLException {
		PreparedStatement stm = DbData.getConnection().prepareStatement("select c.* from Canali c where c.id_server = ?");
		stm.setInt(1, s.getId());
		ResultSet rs = stm.executeQuery();
		
		LinkedList<Canale> c = new LinkedList<Canale>();
		while(rs.next()) {
			c.add(new Canale(rs.getInt("id"),rs.getString("nome"),rs.getInt("id_server"),rs.getInt("id_creatore"),rs.getDate("data_creazione")));
		}
		return c;
	}
}
