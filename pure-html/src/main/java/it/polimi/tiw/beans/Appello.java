package it.polimi.tiw.beans;

import java.sql.Date;

public class Appello {
	private Date data;
	private String nomeCorso;
	public Date getData() {
		return new Date(data.getTime());
	}
	public String getDataString() {
		return new String(data.toString()); 
	}
	public void setData(Date data) {
		this.data = data;
	}
	public void setDataFromString(String data) {
		this.data = Date.valueOf(data);
	}
	public String getNomeCorso() {
		return new String(nomeCorso);
	}
	public void setNomeCorso(String nomeCorso) {
		this.nomeCorso = nomeCorso;
	}
	public Appello(Date data, String nomeCorso) {
		this.data = data;
		this.nomeCorso = nomeCorso;
	}
	public Appello(String data, String nomeCorso) {
		setDataFromString(data);
		this.nomeCorso = nomeCorso; 
	}
	
}