package it.polimi.tiw.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import it.polimi.tiw.beans.Appello;

public class AppelloDAO {
	private Connection connection;
	public AppelloDAO(Connection connection) {
		this.connection = connection;
	}
	/**
	 * Metodo per ottenere la lista degli appelli disponibili per un corso
	 * @param nomeCorso : nome del corso selezionato
	 * @return una lista di tutti e soli gli {@link Appello} del corso selezionato, una lista vuota se non ce ne sono
	 * @throws SQLException
	 */
	public List<Appello> getAppelliByCorso(String nomeCorso) throws SQLException{
		List<Appello> listaAppelli;
		String queryString = "select data, nome_corso from appelli where nome_corso = ? order by data desc";
		try(PreparedStatement ps = connection.prepareStatement(queryString)){
			ps.setString(1, nomeCorso);
			try(ResultSet res = ps.executeQuery()){
				if(!res.isBeforeFirst()) {
					listaAppelli = Collections.emptyList();
				}else {
					listaAppelli = new ArrayList<Appello>();
					while(res.next()) {
						listaAppelli.add(new Appello(
								res.getDate("data"),
								res.getString("nome_corso")
								));
					}
				}
			}
		}
		return listaAppelli;
	}

}
