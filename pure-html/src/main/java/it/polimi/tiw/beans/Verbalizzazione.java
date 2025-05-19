package it.polimi.tiw.beans;

import java.util.UUID;

public class Verbalizzazione {
	private UUID codiceVerbale;
	private int matricolaStudente;
	public String getCodiceVerbale() {
		return codiceVerbale.toString();
	}
	public void setCodiceVerbale(UUID codiceVerbale) {
		this.codiceVerbale = codiceVerbale;
	}
	public int getMatricolaStudente() {
		return matricolaStudente;
	}
	public void setMatricolaStudente(int matricolaStudente) {
		if(matricolaStudente > 100000 && matricolaStudente < 999999)
			this.matricolaStudente = matricolaStudente;
		else
			throw new RuntimeException("Matricola non valida: " + matricolaStudente);
	}
	
}
