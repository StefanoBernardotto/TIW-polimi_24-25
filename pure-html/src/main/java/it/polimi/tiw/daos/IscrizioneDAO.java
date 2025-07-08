package it.polimi.tiw.daos;

import java.util.ArrayList;
import java.util.List;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import it.polimi.tiw.beans.Iscrizione;
import it.polimi.tiw.beans.Studente;
import it.polimi.tiw.misc.Pair;

public class IscrizioneDAO {
	private Connection connection;

	public IscrizioneDAO(Connection connection) {
		this.connection = connection;
	}

	public Iscrizione getEsitoEsame(int matricola, Date dataAppello, String nomeCorso) throws SQLException {
		String queryString = "select * from iscrizioni where matricola_studente = ? and data_appello = ? and nome_corso = ?";
		try (PreparedStatement ps = connection.prepareStatement(queryString)) {
			ps.setInt(1, matricola);
			ps.setDate(2, dataAppello);
			ps.setString(3, nomeCorso);
			try (ResultSet res = ps.executeQuery()) {
				Iscrizione iscrizione;
				if (res.isBeforeFirst()) {
					res.next();
					iscrizione = new Iscrizione(res.getString("nome_corso"), res.getDate("data_appello"),
							res.getInt("matricola_studente"), res.getString("voto"),
							res.getString("stato_pubblicazione"));
				} else {
					iscrizione = null;
				}
				return iscrizione;
			}
		}
	}

	public void rifiutaEsito(int matricola, Date dataAppello, String nomeCorso) throws SQLException {
		String queryString = "update iscrizioni set stato_pubblicazione = 'rifiutato' where nome_corso = ? and data_appello = ? and matricola_studente = ?";
		try (PreparedStatement ps = connection.prepareStatement(queryString)) {
			ps.setString(1, nomeCorso);
			ps.setDate(2, dataAppello);
			ps.setInt(3, matricola);
			ps.executeUpdate();
		}
	}

	public List<Pair<Iscrizione, Studente>> getOrderedIscrittiByAppello(Date dataAppello, String nomeCorso,
			String campoOrdine, boolean desc) throws SQLException {
		String descString = desc ? "desc" : "asc";
		String queryString = "select matricola_studente as matricola, cognome, nome, email, corso_laurea, voto, stato_pubblicazione "
				+ "from iscrizioni join studenti on iscrizioni.matricola_studente = studenti.matricola "
				+ "where iscrizioni.data_appello = ? and iscrizioni.nome_corso = ? " + "order by " + campoOrdine + " "
				+ descString + ";";
		List<Pair<Iscrizione, Studente>> list = new ArrayList<>();
		try (PreparedStatement ps = connection.prepareStatement(queryString)) {
			ps.setDate(1, dataAppello);
			ps.setString(2, nomeCorso);
			try (ResultSet res = ps.executeQuery()) {
				if (res.isBeforeFirst()) {
					while (res.next()) {
						Iscrizione iscrizione = new Iscrizione(
								nomeCorso,
								dataAppello,
								res.getInt("matricola"),
								res.getString("voto"), 
								res.getString("stato_pubblicazione")
						);
						Studente studente = new Studente(
								res.getInt("matricola"), 
								res.getString("nome"),
								res.getString("cognome"), 
								res.getString("email"), 
								null, 
								res.getString("corso_laurea")
						);
						list.add(new Pair<Iscrizione, Studente>(iscrizione, studente));
					}
				}
			}
		}
		return list;
	}
}
