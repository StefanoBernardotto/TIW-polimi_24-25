package it.polimi.tiw.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.UnavailableException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import it.polimi.tiw.beans.Docente;
import it.polimi.tiw.beans.Studente;
import it.polimi.tiw.daos.DocenteDAO;
import it.polimi.tiw.daos.StudenteDAO;
import it.polimi.tiw.misc.DatabaseInit;

/**
 * Servlet per la verifica delle credenziali, sia dello studente che del docente
 */
@WebServlet("/VerificaLogin")
@MultipartConfig
public class VerificaLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection;

	@Override
	public void init() throws UnavailableException {
		this.connection = DatabaseInit.initDB(getServletContext());
	}

	/**
	 * Metodo per la gestione di una richiesta POST. Verifica che il formato dei
	 * parametri sia corretto, se sì verifica le credenziali e risponde al client
	 * con lo status e i dati dell'utente
	 * 
	 * @param profilo  : assume valori "docente" o "studente" in base al profilo
	 *                 richiesto
	 * @param codice   : codice docente o numero di matricola
	 * @param password : password di accesso
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Integer codice;
		String password, profiloString;
		try {
			codice = Integer.parseInt(request.getParameter("codice"));
			password = (String) request.getParameter("password");
			profiloString = (String) request.getParameter("profilo");
			if (password == null || codice == null || profiloString == null) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().println("Parametri mancanti");
				return;
			}
			if ((profiloString.equals("docente") && (codice > 99999999 || codice < 10000000))
					|| (profiloString.equals("studente") && (codice > 999999 || codice < 100000))
					|| !(profiloString.equals("studente") || profiloString.endsWith("docente"))) {
				throw new NumberFormatException();
			}
		} catch (NumberFormatException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Formato parametri errato");
			return;
		}

		String jsonString;
		Gson gson = new GsonBuilder().create();
		try {
			if (profiloString.equals("docente")) {
				DocenteDAO docenteDAO = new DocenteDAO(connection);
				Docente d = docenteDAO.login(codice, password);
				if (d == null) {
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					response.getWriter().println("Credenziali non valide");
					return;
				}
				jsonString = gson.toJson(d);
				request.getSession().setAttribute("docente", d);

			} else {
				StudenteDAO studenteDAO = new StudenteDAO(connection);
				Studente s = studenteDAO.login(codice, password);
				if (s == null) {
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					response.getWriter().println("Credenziali non valide");
					return;
				}
				jsonString = gson.toJson(s);
				request.getSession().setAttribute("studente", s);
			}
			response.setStatus(HttpServletResponse.SC_OK);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonString);
		} catch (SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("Errore nel server");
			return;
		}
	}
	
	@Override
	public void destroy() {
		try {
			if(!this.connection.isClosed()) {
				this.connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
