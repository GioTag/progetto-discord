package AppBusiness;

import java.sql.Date;
import java.sql.Timestamp;

public class Messaggio_privato{
	private int id;
	private Timestamp data;
	private int id_mittente;
	private int id_destinatario;
	private String testo;
	
	public Messaggio_privato (int id, Timestamp data, int id_mittente, int id_destinatario, String testo) {
		this.id=id;
		this.data=data;
		this.id_mittente=id_mittente;
		this.id_destinatario=id_destinatario;
		this.testo=testo;
	}
	
	public int getId() { return id; }
	public Timestamp getData() { return data; }
	public int getid_mittente() { return id_mittente; }
	public int getid_destinatario() { return id_destinatario; }
	public String getTesto() { return testo; }
}
