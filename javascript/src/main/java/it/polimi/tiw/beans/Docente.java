package it.polimi.tiw.beans;

public class Docente {
	private int codiceDocente;
	private String nome;
	private String cognome;
	private String email;
	private String password;
	public int getCodiceDocente() {
		return codiceDocente;
	}
	public void setCodiceDocente(int codiceDocente) {
		if(codiceDocente > 10000000 && codiceDocente < 99999999)
			this.codiceDocente = codiceDocente;
		else
			throw new RuntimeException("Codice docente non valido: " + codiceDocente);
	}
	public String getNome() {
		return new String(nome);
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCognome() {
		return new String(cognome);
	}
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	public String getEmail() {
		return new String(email);
	}
	public void setEmail(String email) {
		if(email.contains("@") && email.split("@").length == 2 && email.split("@")[1].contains("."))
			this.email = email;
		else
			throw new RuntimeException("Email non valida: " + email);
	}
	public String getPassword() {
		return new String(password);
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Docente(int codiceDocente, String nome, String cognome, String email, String password) {
		this.setCodiceDocente(codiceDocente);
		this.nome = nome;
		this.cognome = cognome;
		this.setEmail(email);
		this.password = password;
	}
}
