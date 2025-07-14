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
	
	/**
	 * Metodo per verificare le credenziali di uno studente
	 * @param matricola : matricola dello studente
	 * @param psw : password di accesso
	 * @return uno {@link Studente} relativo alle credenziali di accesso, {@code null} se non esiste
	 * @throws SQLException
	 */
	public Studente login(int matricola, String psw) throws SQLException{
		String queryString = "select * from Studenti where matricola = ? and password = ?";
		try(PreparedStatement ps = connection.prepareStatement(queryString)) {
			ps.setInt(1, matricola);
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
							"",
							res.getString("corso_laurea"));
				}
			}
		}
	}
	
	/**
	 * Metodo per ottenere le informazioni di uno studente data la sua matricola
	 * @param matricola : matricola dello studente
	 * @return lo {@link Studente} relativo alla matricola
	 * @throws SQLException
	 */
	public Studente getStudenteByMatricola(int matricola) throws SQLException{
		String queryString = "select * from Studenti where matricola = ?";
		try(PreparedStatement ps = connection.prepareStatement(queryString)) {
			ps.setInt(1, matricola);
			try(ResultSet res = ps.executeQuery()){
				if(!res.isBeforeFirst()) {
					return null;
				}else {
					res.next();
					return new Studente(
							res.getInt("matricola"),
							res.getString("nome"),
							res.getString("cognome"),
							res.getString("email"),
							"",
							res.getString("corso_laurea"));
				}
			}
		}
	}

}