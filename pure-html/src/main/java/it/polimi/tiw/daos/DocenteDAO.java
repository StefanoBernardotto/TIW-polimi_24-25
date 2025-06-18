package it.polimi.tiw.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import it.polimi.tiw.beans.Docente;

public class DocenteDAO {
	private final Connection connection;
	public DocenteDAO(Connection c) {
		this.connection = c;
	}

	public Docente login(int cod, String psw) throws SQLException{
		String queryString = "select * from Docenti where codice_docente = ? and password = ?";
		try(PreparedStatement ps = connection.prepareStatement(queryString)) {
			ps.setInt(1, cod);
			ps.setString(2, psw);
			try(ResultSet res = ps.executeQuery()){
				if(!res.isBeforeFirst()) {
					return null;  // credenziali non valide, login fallito
				}else {
					res.next();  // credenziali valide, restituisce il docente
					return new Docente(
							res.getInt("codice_docente"),
							res.getString("nome"),
							res.getString("cognome"),
							res.getString("email"),
							"");
				}
			}
		}
	}
	
}
