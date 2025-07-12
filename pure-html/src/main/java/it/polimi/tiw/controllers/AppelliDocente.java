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

import it.polimi.tiw.beans.Appello;
import it.polimi.tiw.daos.AppelloDAO;
import it.polimi.tiw.misc.DatabaseInit;
import it.polimi.tiw.misc.ThymeleafInit;

/**
 * Servlet per la pagina Appelli docente
 */
@WebServlet("/AppelliDocente")
public class AppelliDocente extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection;
	private TemplateEngine templateEngine;
	
	@Override
	public void init() throws UnavailableException {
		connection = DatabaseInit.initDB(getServletContext());
		templateEngine = ThymeleafInit.initialize(getServletContext());
	}
	
	/**
	 * Gestione della richiesta GET. Verifica che il login sia effettuato, altrimenti rimanda alla pagina di login.
	 * Se il login è valido, verifica i parametri e mostra gli appelli per il corso scelto (template "docente/appelli_docente").
	 * @param "nomeCorso" : nome del corso selezionato, di cui si vogliono vedere gli appelli disponibili.
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		IWebExchange webExchange = JakartaServletWebApplication.buildApplication(getServletContext())
				.buildExchange(request, response);
		WebContext context = new WebContext(webExchange);

		HttpSession session = request.getSession();
		Integer codiceDocente = (Integer) session.getAttribute("codice_docente");
		String nomeDocente = (String) session.getAttribute("nome_docente");
		if (session.isNew() || codiceDocente == null || nomeDocente == null) {
			response.sendRedirect(request.getContextPath() + "/LoginDocente");
			return;
		}
		
		context.setVariable("nome", nomeDocente);

		String nomeCorso = request.getParameter("nomeCorso");
		if (nomeCorso == null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parametro mancante");
			return;
		}

		AppelloDAO appelloDAO = new AppelloDAO(connection);
		try {
			List<Appello> listaAppelli = appelloDAO.getAppelliByCorso(nomeCorso);
			if (listaAppelli.isEmpty()) {
				context.setVariable("messaggioListaVuota", "Nessun appello per il corso: " + nomeCorso);
			} else {
				context.setVariable("listaAppelli", listaAppelli);
			}
			templateEngine.process("docente/appelli_docente", context, response.getWriter());
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore nel collegamento al database");
			return;
		}
	}
	
	@Override
	public void destroy() {
		try {
			if(!connection.isClosed()) {
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
