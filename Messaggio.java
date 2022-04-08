package AppBusiness;

import java.sql.Date;

public abstract class Messaggio {
	private String testo;
	private Date data;
	private String id_server;
	private String id_mittente;
	
	public Messaggio(String testo,Date data,String id_server,String id_mittente) {
		
	}
}
