package it.polimi.tiw.beans;

import java.sql.Date;
import java.sql.Time;
import java.util.UUID;

public class Verbale {
	private UUID codice;
	private Date dataCreazione;
	private Time oraCreazione;
	private String nomeCorso;
	private Date dataAppello;
	public String getCodiceToString() {
		return new String(codice.toString());
	}
	public void setCodice(UUID codice) {
		this.codice = codice;
	}
	public Date getDataCreazione() {
		return new Date(dataCreazione.getTime());
	}
	public void setDataCreazione(Date dataCreazione) {
		this.dataCreazione = dataCreazione;
	}
	public Time getOraCreazione() {
		return new Time(oraCreazione.getTime());
	}
	public void setOraCreazione(Time oraCreazione) {
		this.oraCreazione = oraCreazione;
	}
	public String getNomeCorso() {
		return new String(nomeCorso);
	}
	public void setNomeCorso(String nomecorso) {
		this.nomeCorso = nomecorso;
	}
	public Date getDataAppello() {
		return new Date(dataAppello.getTime());
	}
	public void setDataAppello(Date dataAppello) {
		this.dataAppello = dataAppello;
	}
	public Verbale(Date dataCreazione, Time oraCreazione, String nomecorso, Date dataAppello) {
		this.codice = UUID.randomUUID();
		this.dataCreazione = dataCreazione;
		this.oraCreazione = oraCreazione;
		this.nomeCorso = nomecorso;
		this.dataAppello = dataAppello;
	}
	public Verbale(UUID codice, Date dataCreazione, Time oraCreazione, String nomecorso, Date dataAppello) {
		this.codice = codice;
		this.dataCreazione = dataCreazione;
		this.oraCreazione = oraCreazione;
		this.nomeCorso = nomecorso;
		this.dataAppello = dataAppello;
	}
	
	
}
