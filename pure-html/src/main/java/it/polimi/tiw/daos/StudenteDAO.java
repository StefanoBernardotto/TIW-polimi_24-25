package it.polimi.tiw.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import it.polimi.tiw.beans.Studente;

public class StudenteDAO {
	private final Connection connection;
	
	public StudenteDAO(Connection connection) {
		this.connection = connection;
	}
	
	public Studente login(int mat, String psw) throws SQLException{
		String queryString = "select * from Studenti where matricola = ? and password = ?";
		try(PreparedStatement ps = connection.prepareStatement(queryString)) {
			ps.setInt(1, mat);
			ps.setString(2, psw);
			try(ResultSet res = ps.executeQuery()){
				if(!res.isBeforeFirst()) {
					return null;  // credenziali non valide, login fallito
				}else {
					res.next();  // credenziali valide, restituisce lo studente
					return new Studente(
							res.getInt("matricola"),
							res.getString("nome"),
							res.getString("cognome"),
							res.getString("email"),
							res.getString("password"),
							res.getString("corso_laurea"));
				}
			}
		}
	}

}
