package it.polimi.tiw.beans;

public class Studente {
	private int matricola;
	private String nome;
	private String cognome;
	private String email;
	private String password;
	private String corsoLaurea;
	public String getCorsoLaurea() {
		return new String(corsoLaurea);
	}
	public void setCorsoLaurea(String corsoLaurea) {
		this.corsoLaurea = corsoLaurea;
	}
	public int getMatricola() {
		return matricola;
	}
	public void setMatricola(int matricola) {
		if(matricola > 100000 && matricola < 999999)
			this.matricola = matricola;
		else
			throw new RuntimeException("Matricola non valida: " + matricola);
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
	public Studente(int matricola, String nome, String cognome, String email, String password, String corsoLaurea) {
		this.setMatricola(matricola);
		this.nome = nome;
		this.cognome = cognome;
		this.setEmail(email);
		this.password = password;
		this.corsoLaurea = corsoLaurea;
	}
}
