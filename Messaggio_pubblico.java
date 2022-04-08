package AppBusiness;

import java.sql.Date;
import java.sql.Timestamp;

public class Messaggio_pubblico{
	private int id;
	private Timestamp data;
	private Utente mittente;
	private String testo;
	private int id_canale;
	
	public Messaggio_pubblico (int id, Timestamp data, Utente mittente, String testo, int id_canale) {
		this.id=id;
		this.data=data;
		this.mittente=mittente;
		this.testo=testo;
		this.id_canale=id_canale;
	}
	
	public int getId() { return id; }
	public Timestamp getData() { return data; }
	public Utente get_mittente() { return mittente; }
	public String getTesto() { return testo; }
	public int getid_canale() { return id_canale; }
}