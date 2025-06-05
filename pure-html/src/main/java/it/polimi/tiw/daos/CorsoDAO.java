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
	
	public List<Corso> getCorsiByStudente(int matricola) throws SQLException{
		List<Corso> listaCorsi;
		String queryString = "select corsi.nome as nome, docenti.nome as nome_doc, docenti.cognome as cognome_doc, docenti.codice_docente as codice_doc "
				+ "from iscrizioni_corsi join corsi on nome = nome_corso join docenti on docenti.codice_docente = corsi.codice_docente "
				+ "where matricola_studente = ?";
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
}
