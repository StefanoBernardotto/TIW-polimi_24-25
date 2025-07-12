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

import it.polimi.tiw.beans.Docente;
import it.polimi.tiw.daos.DocenteDAO;
import it.polimi.tiw.misc.DatabaseInit;
import it.polimi.tiw.misc.ThymeleafInit;

/**
 * Servlet per l'autenticazione del docente
 */
@WebServlet("/LoginDocente")
public class LoginDocente extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection;
	private TemplateEngine templateEngine;

	@Override
	public void init() throws UnavailableException {
		connection = DatabaseInit.initDB(getServletContext());
		templateEngine = ThymeleafInit.initialize(getServletContext());
	}

	/**
	 * Gestione della richiesta GET: mostra il template "docente/login_docente.html" per il login del docente
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		IWebExchange webExchange = JakartaServletWebApplication.buildApplication(getServletContext())
				.buildExchange(request, response);
		WebContext context = new WebContext(webExchange);
		templateEngine.process("docente/login_docente", context, response.getWriter());
	}

	/**
	 * Gestione della richiesta POST: verifica i parametri inviati.
	 * Se errati mostra il messaggio di errore appropriato, se corretti crea la sessione e manda redirect alla home
	 * @param "codice_docente": codice identificativo del docente
	 * @param "password": password di accesso del docente
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		IWebExchange webExchange = JakartaServletWebApplication.buildApplication(getServletContext())
				.buildExchange(request, response);
		WebContext context = new WebContext(webExchange);

		DocenteDAO docenteDAO = new DocenteDAO(connection);
		Integer codice = null;
		String password = null;

		try {
			codice = Integer.parseInt(request.getParameter("codice_docente"));
			password = (String) request.getParameter("password");
			if (password == null || codice == null) {
				context.setVariable("messaggioErroreLogin", "Parametri mancanti");
				templateEngine.process("docente/login_docente", context, response.getWriter());
				return;
			}
		} catch (NumberFormatException e) {
			context.setVariable("messaggioErroreLogin", "Formato matricola non valido, inserire solo valori numerici");
			templateEngine.process("docente/login_docente", context, response.getWriter());
			return;
		}

		try {
			Docente d = docenteDAO.login(codice, password);
			if (d == null) {
				// Credenziali non valide
				context.setVariable("messaggioErroreLogin", "Credenziali non valide, si prega di riprovare");
				templateEngine.process("docente/login_docente", context, response.getWriter());
				return;
			}
			// Credenziali valide
			HttpSession session = request.getSession();
			session.setAttribute("codice_docente", d.getCodiceDocente());
			session.setAttribute("nome_docente", d.getNome() + " " + d.getCognome());
			response.sendRedirect(request.getContextPath() + "/HomeDocente");
		} catch (SQLException e) {
			context.setVariable("messaggioErroreLogin", "Errore durante il login, si prega di riprovare");
			templateEngine.process("docente/login_docente", context, response.getWriter());
			return;
		}
	}

	@Override
	public void destroy() {
		try {
			if (!connection.isClosed()) {
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
