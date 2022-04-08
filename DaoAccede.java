package Dao;

import java.sql.*;
import java.util.LinkedList;

import AppBusiness.Canale;
import AppBusiness.Ruolo;
/*Classe contente tutti i metodi relativi all'associazione Accede
 * 
 * getCanaliAcceduti(Ruolo r) - Ottiene una lista dei canali accedduti dal ruolo r
 * materializeAccede(int r,int c) - crea una relazione d'accesso tra il ruolo con id r e il canale con id c
 * removeAccede(int r,int c) - analogo del materialize ma rimuove l'associazione
 */

public class DaoAccede {
	//Metodo che ottiene
	public static LinkedList<Canale> getCanaliAcceduti(Ruolo r) throws SQLException {
		PreparedStatement stm = DbData.getConnection().prepareStatement("select c.*"
																	  + "from canali c,accede m "
																	  + "where m.id_ruolo =? and m.id_canale = c.id");
		
		stm.setInt(1, r.getId());
		ResultSet rs = stm.executeQuery();
		
		LinkedList<Canale> c = new LinkedList<Canale>();
		while(rs.next()) {
			c.add(new Canale(rs.getInt("id"),rs.getString("nome"),rs.getInt("id_server"),rs.getInt("id_creatore"),rs.getDate("data_creazione")));
		}
		return c;
	}	
	
	public static void materializeAccede(int r, int c) throws SQLException {
		PreparedStatement stm = DbData.getConnection().prepareStatement("Insert into accede "
				  													  + "Values (?,?)");

		
		stm.setInt(1, c);
		stm.setInt(2, r);
		stm.executeUpdate();
	}
	public static void removeAccede(int r, int c) throws SQLException {
		PreparedStatement stm = DbData.getConnection().prepareStatement("DELETE FROM accede "
				  													  + "WHERE id_ruolo = ? and id_canale = ?");

		stm.setInt(1, r);
		stm.setInt(2, c);
		stm.executeUpdate();
	}
}
