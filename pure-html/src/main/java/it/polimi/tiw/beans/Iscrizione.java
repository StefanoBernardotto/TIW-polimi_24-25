package it.polimi.tiw.beans;

import java.sql.Date;

public class Iscrizione {
	private String nomeCorso;
	private Date dataAppello;
	private int matricolaStudente;
	private String voto;
	private String statoPubblicazione;

	public String getNomeCorso() {
		return new String(nomeCorso);
	}

	public void setNomeCorso(String nomeCorso) {
		this.nomeCorso = nomeCorso;
	}

	public Date getDataAppello() {
		return new Date(dataAppello.getTime());
	}

	public void setDataAppello(Date dataAppello) {
		this.dataAppello = dataAppello;
	}

	public int getMatricolaStudente() {
		return matricolaStudente;
	}

	public void setMatricolaStudente(int matricolaStudente) {
		if (matricolaStudente > 100000 && matricolaStudente < 999999)
			this.matricolaStudente = matricolaStudente;
		else
			throw new RuntimeException("Matricola non valida: " + matricolaStudente);
	}

	public String getVoto() {
		return new String(voto);
	}

	public void setVoto(String voto) {
		try {
			int v = Integer.parseInt(voto);
			if (v >= 18 && v <= 30) {
				this.voto = voto;
			} else {
				throw new NumberFormatException();
			}
		} catch (NumberFormatException e) {
			if (voto.equals("assente") || voto.equals("rimandato") || voto.equals("riprovato")
					|| voto.equals("30 e lode")) {
				this.voto = voto;
			} else {
				throw new RuntimeException("Voto non valido: " + voto);
			}
		}
		this.voto = voto;
	}

	public String getStatoPubblicazione() {
		return new String(statoPubblicazione);
	}

	public void setStatoPubblicazione(String statoPubblicazione) {
		if (statoPubblicazione.equals("non inserito") || statoPubblicazione.equals("inserito")
				|| statoPubblicazione.equals("pubblicato") || statoPubblicazione.equals("rifiutato")
				|| statoPubblicazione.equals("verbalizzato")) {
			this.statoPubblicazione = statoPubblicazione;
		} else {
			throw new RuntimeException("Stato di pubblicazione non valido: " + statoPubblicazione);
		}
	}

	public Iscrizione(String nomeCorso, Date dataAppello, int matricolaStudente, String voto,
			String statoPubblicazione) {
		this.nomeCorso = nomeCorso;
		this.dataAppello = dataAppello;
		this.matricolaStudente = matricolaStudente;
		this.setVoto(voto);
		this.setStatoPubblicazione(statoPubblicazione);
	}
	
	public boolean isPubblicato() {
		return this.statoPubblicazione.equals("pubblicato") || this.statoPubblicazione.equals("verbalizzato") || this.statoPubblicazione.equals("rifiutato");
	}
	
	public boolean isRifiutato() {
		return this.statoPubblicazione.equals("rifiutato");
	}
	
	public boolean isVerbalizzato() {
		return this.statoPubblicazione.equals("verbalizzato");
	}

}
