package AppBusiness;

import java.sql.Date;
import java.sql.SQLException;
import java.util.LinkedList;

import Dao.DbData;

public class Server {
	private int id;
	private String nome;
	private Date data_creazione;
	private int id_creatore;
	private LinkedList<Canale> canaliAccessibili = new LinkedList<Canale>();
	private LinkedList<Utente> utenti = new LinkedList<Utente>();
	
	public Server(String nome,Date data_creazione,int id_creatore,int id) {
		this.id = id;
		this.nome = nome;
		this.data_creazione= data_creazione;
		this.id_creatore = id_creatore;	
	}
		
	public int getId() { return id; }
	public String getNome() { return nome; }
	public Date getData_creazione(){ return data_creazione; }
	public int getId_creatore() { return id_creatore; }
	
	public void addCanale(Canale c) { canaliAccessibili.add(c); }
	public void addUtente(Utente u) { utenti.add(u); }
	
	public void removeCanale(Canale c) { canaliAccessibili.remove(c); }
	public void removeUtente(Utente u) { utenti.remove(u); }
	
	public LinkedList<Canale> getCanali() { return canaliAccessibili; }
	public LinkedList<Utente> getUtenti(){ return utenti; }
	
}
	