package it.polimi.tiw.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.polimi.tiw.beans.Corso;

public class CorsoDAO {
	private Connection connection;
	
	public CorsoDAO(Connection connection) {
		this.connection = connection;
	}
	
	/**
	 * Metodo per ottenere tutti i corsi a cui è iscritto uno studente
	 * @param matricola : matricola dello studente selezionato
	 * @return una lista di tutti e soli i {@link Corso} a cui è iscritto lo studente, una lista vuota se non è iscritto a nessun corso
	 * @throws SQLException
	 */
	public List<Corso> getCorsiByStudente(int matricola) throws SQLException{
		List<Corso> listaCorsi;
		String queryString = "select corsi.nome as nome, docenti.nome as nome_doc, docenti.cognome as cognome_doc, docenti.codice_docente as codice_doc "
				+ "from iscrizioni_corsi join corsi on nome = nome_corso join docenti on docenti.codice_docente = corsi.codice_docente "
				+ "where matricola_studente = ? "
				+ "order by corsi.nome desc";
		try(PreparedStatement ps = connection.prepareStatement(queryString)){
			ps.setInt(1, matricola);			
			try(ResultSet res = ps.executeQuery()){
				if(!res.isBeforeFirst()) {
					listaCorsi = Collections.emptyList();
				}else{
					listaCorsi = new ArrayList<Corso>();
					while(res.next()) {
						listaCorsi.add(new Corso(
								res.getString("nome"),
								res.getInt("codice_doc"),
								res.getString("nome_doc") + " " + res.getString("cognome_doc")
								));
					}
				}
				return listaCorsi;
			}
		}
	}
	
	/**
	 * Metodo per ottenere tutti i corsi tenuti da un docente
	 * @param codiceDocente : codice del docente selezionato
	 * @return una lista di tutti e soli i {@link Corso} tenuti dal docente, una lista vuota se non tiene nessun corso
	 * @throws SQLException
	 */
	public List<Corso> getCorsiByDocente(int codiceDocente) throws SQLException{
		List<Corso> listaCorsi;
		String queryString = "select corsi.nome as nome from corsi where codice_docente = ? order by corsi.nome desc";
		try(PreparedStatement ps = connection.prepareStatement(queryString)){
			ps.setInt(1, codiceDocente);			
			try(ResultSet res = ps.executeQuery()){
				if(!res.isBeforeFirst()) {
					listaCorsi = Collections.emptyList();
				}else{
					listaCorsi = new ArrayList<Corso>();
					while(res.next()) {
						listaCorsi.add(new Corso(res.getString("nome")));
					}
				}
				return listaCorsi;
			}
		}
	}
}
