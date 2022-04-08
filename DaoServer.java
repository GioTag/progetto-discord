package Dao;

import java.sql.*;

import AppBusiness.Server;

public class DaoServer {
	public static boolean materializeServer(Server s) throws SQLException {
		Connection conn = DbData.getConnection();
		
		PreparedStatement stm = conn.prepareStatement("SELECT nome FROM server WHERE nome=?");
		stm.setString(1,s.getNome());
		ResultSet rs = stm.executeQuery();
		
		if(rs.next()) {
			return false;
		}
		
		//creiamo il server
		
		//id del creatore
		
		stm = conn.prepareStatement("INSERT INTO server(nome,data_creazione,id_creatore) VALUES (?,?,?)");
		Date data = new Date(System.currentTimeMillis());//ottengo la data attuale
		
		stm.setString(1, s.getNome());
		stm.setDate(2, data);
		stm.setInt(3, DbData.user.getId());
		
		return true;
	}	

}
