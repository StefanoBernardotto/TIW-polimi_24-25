package it.polimi.tiw.beans;

import java.sql.Date;

public class Appello {
	private Date data;
	private String nomeCorso;
	public Date getData() {
		return new Date(data.getTime());
	}
	public void setData(Date data) {
		this.data = data;
	}
	public String getNomeCorso() {
		return new String(nomeCorso);
	}
	public void setNomeCorso(String nomeCorso) {
		this.nomeCorso = nomeCorso;
	}
	
}