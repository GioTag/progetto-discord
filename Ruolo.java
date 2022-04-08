package AppBusiness;

import java.sql.Date;

public class Ruolo {
	private String nome;
	private int id_creatore;
	private int id_server;
	private int id;
	private String tipo;
	private Date data_creazione;
	
	public Ruolo(int id,String nome, int id_creatore, int id_server,Date data_creazione,String tipo) {
		this.id = id;
		this.nome=nome;
		this.id_creatore=id_creatore;
		this.id_server=id_server;
		this.tipo = tipo;
		this.data_creazione = data_creazione;
	}
	public int getId() { return id; }
	public String getNome() { return nome; }
	public int getId_creatore() { return id_creatore; }
	public int getId_server() { return id_server; }
	public String getTipo() { return tipo; }
	public Date getData() { return data_creazione; }
}
