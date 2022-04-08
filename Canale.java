package AppBusiness;

import java.sql.Date;

public class Canale {
	private int id;
	private String nome;
	private Date data_creazione;
	private int id_server;
	private int id_creatore;
	
	public Canale(int id,String nome,int server,int creatore,Date data_creazione) {
		this.id = id;
		this.nome = nome;
		this.id_server = server;
		this.id_creatore = creatore;
		this.data_creazione = data_creazione;
	}
	public int getId() { return id; }
	public String getNome() { return nome; }
	public int getServer() { return id_server; }
	public int getCreatore() { return id_creatore; }
	public Date getDataCreazione() { return data_creazione; }
}
