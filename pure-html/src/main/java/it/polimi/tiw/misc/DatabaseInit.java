package it.polimi.tiw.misc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.eclipse.jdt.internal.compiler.ast.ThisReference;

import jakarta.servlet.ServletContext;
import jakarta.servlet.UnavailableException;

public class DatabaseInit {
	public static Connection initDB(ServletContext ctx) throws UnavailableException{
		Connection connection = null;
		try {
			String driver = ctx.getInitParameter("dbDriver");
			String url = ctx.getInitParameter("dbUrl");
			String user = ctx.getInitParameter("dbUser");
			String password = ctx.getInitParameter("dbPassword");
			Class.forName(driver);
			connection = DriverManager.getConnection(url, user, password);
		}catch (ClassNotFoundException e) {
			throw new UnavailableException("Driver non trovato");
		}catch(SQLException e) {
			throw new UnavailableException("Errore nella connessione al database");
		}
		return connection;
	}
}