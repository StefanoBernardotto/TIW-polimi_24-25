package it.polimi.tiw.beans;

public class Corso {
	private String nome;
	private int codiceDocente;
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public int getCodiceDocente() {
		return codiceDocente;
	}
	public void setCodiceDocente(int codiceDocente) {
		if(codiceDocente > 10000000 && codiceDocente < 99999999)
			this.codiceDocente = codiceDocente;
		else
			throw new RuntimeException("Codice docente non valido: " + codiceDocente);
	}
	public Corso(String nome, int codiceDocente) {
		this.nome = nome;
		this.setCodiceDocente(codiceDocente);
	}
}
