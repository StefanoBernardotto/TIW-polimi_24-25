package it.polimi.tiw.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.UnavailableException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import it.polimi.tiw.beans.Studente;
import it.polimi.tiw.daos.StudenteDAO;
import it.polimi.tiw.misc.DatabaseInit;
import it.polimi.tiw.misc.ThymeleafInit;

/**
 * Servlet per l'autenticazione dello studente
 */
@WebServlet("/LoginStudente")
public class LoginStudente extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection;
	private TemplateEngine templateEngine;
	
	@Override
	public void init() throws UnavailableException {
		connection = DatabaseInit.initDB(getServletContext());
		templateEngine = ThymeleafInit.initialize(getServletContext());
	}
	
	/**
	 * Gestione della richiesta GET: mostra il template "studente/login_studente.html" per il login dello studente
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		IWebExchange webExchange = JakartaServletWebApplication.buildApplication(getServletContext())
				.buildExchange(request, response);
		WebContext context = new WebContext(webExchange);
		templateEngine.process("studente/login_studente", context, response.getWriter());
	}
	
	/**
	 * Gestione della richiesta POST: verifica i parametri inviati.
	 * Se errati mostra il messaggio di errore appropriato, se corretti crea la sessione e manda redirect alla home
	 * @param "matricola": codice identificativo dello studente
	 * @param "password": password di accesso del docente
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		IWebExchange webExchange = JakartaServletWebApplication.buildApplication(getServletContext())
				.buildExchange(request, response);
		WebContext context = new WebContext(webExchange);
		
		StudenteDAO studenteDAO = new StudenteDAO(connection);
		int matricola = 0;
		String password = null;
		
		try {
			matricola = Integer.parseInt(request.getParameter("matricola"));
			password = (String) request.getParameter("password");
			if(password == null) {
				context.setVariable("messaggioErroreLogin", "Parametri mancanti");
				templateEngine.process("studente/login_studente", context, response.getWriter());
				return;
			}
		}catch(NumberFormatException e) {
			context.setVariable("messaggioErroreLogin", "Formato matricola non valido, inserire solo valori numerici");
			templateEngine.process("studente/login_studente", context, response.getWriter());
			return;
		}
		
		try {
			Studente s = studenteDAO.login(matricola, password);
			if(s == null) {
				// Credenziali errate
				context.setVariable("messaggioErroreLogin", "Credenziali non valide, si prega di riprovare");
				templateEngine.process("studente/login_studente", context, response.getWriter());
				return;
			}
			// Credenziali valide
			HttpSession session = request.getSession();
			session.setAttribute("matricola_studente", s.getMatricola());
			response.sendRedirect(request.getContextPath() + "/HomeStudente");
		}catch(SQLException e) {
			context.setVariable("messaggioErroreLogin", "Errore durante il login, si prega di riprovare");
			templateEngine.process("studente/login_studente", context, response.getWriter());
			return;
		}
	}
	
	@Override
	public void destroy() {
		try {
			if(!connection.isClosed()) {
				connection.close();
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
