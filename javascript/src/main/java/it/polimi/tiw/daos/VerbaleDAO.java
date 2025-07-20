package it.polimi.tiw.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.polimi.tiw.beans.Verbale;

public class VerbaleDAO {
	private Connection connection;
	
	public VerbaleDAO(Connection con) {
		this.connection = con;
	}
	
	/**
	 * Metodo per ottenere un verbale dato il suo codice
	 * @param codiceVerbale : codice del verbale
	 * @return il {@link Verbale} con il codice passato
	 * @throws SQLException
	 */
	public Verbale getVerbaleByCodice(UUID codiceVerbale) throws SQLException {
		String queryString = "select * from verbali where codice = ?;";
		try(PreparedStatement ps = connection.prepareStatement(queryString)){
			ps.setString(1, codiceVerbale.toString());
			try(ResultSet res = ps.executeQuery()){
				if(res.isBeforeFirst()) {
					res.next();
					return new Verbale(
						codiceVerbale,
						res.getDate("data_creazione"),
						res.getTime("ora_creazione"),
						res.getString("nome_corso"),
						res.getDate("data_appello")
					);
				}
			}
		}
		return null;
	}
	
	/**
	 * Metodo per ottenere i verbali di tutti i corsi di un docente
	 * @param codiceDocente : codice del docente
	 * @return una lista di tutti e soli i {@link Verbale} relativi ai corsi del docente selezionato
	 * @throws SQLException
	 */
	public List<Verbale> getOrderdVerbaliByDocente(int codiceDocente) throws SQLException {
		String queryString = "select codice, data_creazione, ora_creazione, data_appello, nome_corso from verbali join corsi on verbali.nome_corso = corsi.nome where codice_docente = ? order by nome_corso, data_appello";
		try (PreparedStatement ps = connection.prepareStatement(queryString)) {
			ps.setInt(1, codiceDocente);
			try (ResultSet res = ps.executeQuery()) {
				if (res.isBeforeFirst()) {
					List<Verbale> listaVerbali = new ArrayList<>();
					while (res.next()) {
						try {
							listaVerbali.add(new Verbale(
									UUID.fromString(res.getString("codice")),
									res.getDate("data_creazione"),
									res.getTime("ora_creazione"),
									res.getString("nome_corso"),
									res.getDate("data_appello")
							));
						} catch (IllegalArgumentException e) {
							throw new SQLException("Eccezione nel caricamento UUID: " + e.getMessage());
						}
					}
					return listaVerbali;
				} else {
					return Collections.emptyList();
				}
			}
		}
	}
	
	/**
	 * Metodo per ottenere i numeri di matricola degli studenti inseriti in un verbale
	 * @param codiceVerbale : codice del verbale
	 * @return un array contenente tutti e soli i numeri di matricola degli studenti inseriti nel {@link Verbale} con il codice passato
	 * @throws SQLException
	 */
	public Integer[] getMatricoleByCodice(UUID codiceVerbale) throws SQLException {
		String queryString = "select matricola_studente from verbalizzazioni where codice_verbale = ?;";
		try(PreparedStatement ps = connection.prepareStatement(queryString)){
			ps.setString(1, codiceVerbale.toString());
			try(ResultSet res = ps.executeQuery()){
				if(res.isBeforeFirst()) {
					List<Integer> listaMatricole = new ArrayList<>();
					while(res.next()) {
						listaMatricole.add(res.getInt("matricola_studente"));
					}
					return listaMatricole.toArray(new Integer[0]);
				}else {
					throw new SQLException("Nessuna riga ottenuta");
				}
			}
		}
	}	
}