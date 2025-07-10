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
import java.util.List;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import it.polimi.tiw.beans.Verbale;
import it.polimi.tiw.daos.VerbaleDAO;
import it.polimi.tiw.misc.DatabaseInit;
import it.polimi.tiw.misc.ThymeleafInit;

/**
 * Servlet per la pagina Verbali del docente
 */
@WebServlet("/Verbali")
public class Verbali extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection;
	private TemplateEngine templateEngine;
	
	@Override
	public void init() throws UnavailableException {
		connection = DatabaseInit.initDB(getServletContext());
		templateEngine = ThymeleafInit.initialize(getServletContext());
	}

	/**
	 * Gestione della richiesta GET. Verifica se il login è effettuato, altrimenti rimanda alla pagina di login.
	 * Se il login è valido mostra i verbali relativi ai corsi del docente (template "docente/verbali")
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		IWebExchange webExchange = JakartaServletWebApplication.buildApplication(getServletContext())
				.buildExchange(request, response);
		WebContext context = new WebContext(webExchange);

		HttpSession session = request.getSession();
		Integer codiceDocente = (Integer) session.getAttribute("codice_docente");
		if (session.isNew() || codiceDocente == null) {
			response.sendRedirect(request.getContextPath() + "/LoginDocente");
			return;
		}
		
		VerbaleDAO verbaleDAO = new VerbaleDAO(connection);
		try {
			List<Verbale> listaVerbali = verbaleDAO.getOrderdVerbaliByDocente(codiceDocente);
			context.setVariable("listaVerbali", listaVerbali);
			templateEngine.process("docente/verbali", context, response.getWriter());
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore nella connessione al database");
		}
	}
}
