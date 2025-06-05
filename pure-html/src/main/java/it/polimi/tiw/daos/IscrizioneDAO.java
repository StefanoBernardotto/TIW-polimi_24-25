package it.polimi.tiw.daos;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import it.polimi.tiw.beans.Iscrizione;

public class IscrizioneDAO {
	private Connection connection;
	public IscrizioneDAO(Connection connection) {
		this.connection = connection;
	}
	
	public Iscrizione getEsitoEsame(int matricola, Date dataAppello, String nomeCorso) throws SQLException {
		String queryString = "select * from iscrizioni where matricola_studente = ? and data_appello = ? and nome_corso = ?";
		try(PreparedStatement ps = connection.prepareStatement(queryString)){
			ps.setInt(1, matricola);
			ps.setDate(2, dataAppello);
			ps.setString(3, nomeCorso);
			try(ResultSet res = ps.executeQuery()){
				Iscrizione iscrizione;
				if(res.isBeforeFirst()) {
					res.next();
					iscrizione = new Iscrizione(
							res.getString("nome_corso"),
							res.getDate("data_appello"),
							res.getInt("matricola_studente"),
							res.getString("voto"),
							res.getString("stato_pubblicazione")
							);
				}else {
					iscrizione = null;
				}
				return iscrizione;
			}
		}
	}
}
