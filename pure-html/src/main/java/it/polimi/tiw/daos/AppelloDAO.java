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
	
	public List<Appello> getAppelliByCorso(String nomecorso) throws SQLException{
		List<Appello> listaAppelli;
		String queryString = "select data, nome_corso from appelli where nome_corso = ?";
		try(PreparedStatement ps = connection.prepareStatement(queryString)){
			ps.setString(1, nomecorso);
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
