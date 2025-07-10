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

import it.polimi.tiw.beans.Corso;
import it.polimi.tiw.daos.CorsoDAO;
import it.polimi.tiw.misc.DatabaseInit;
import it.polimi.tiw.misc.ThymeleafInit;

/**
 * Servlet per la home page dello studente
 */
@WebServlet("/HomeStudente")
public class HomeStudente extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	private Connection connection;

	@Override
	public void init() throws UnavailableException {
		connection = DatabaseInit.initDB(getServletContext());
		templateEngine = ThymeleafInit.initialize(getServletContext());
	}

	/**
	 * Gestione della richiesta GET: verifica che il login sia stato effettuato.
	 * Se sì mostra la lista dei corsi a cui lo studente è iscritto (template "studente/home_studente"), altrimenti reindirizza alla pagina di login
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		IWebExchange webExchange = JakartaServletWebApplication.buildApplication(getServletContext())
				.buildExchange(request, response);
		WebContext context = new WebContext(webExchange);

		HttpSession session = request.getSession();
		Integer matricola = (Integer) session.getAttribute("matricola_studente");
		if (!session.isNew() && matricola != null) {
			CorsoDAO corsoDAO = new CorsoDAO(connection);
			try {
				List<Corso> listaCorsi = corsoDAO.getCorsiByStudente(matricola);
				if(!listaCorsi.isEmpty()) {
					context.setVariable("listaCorsi", listaCorsi);
				} else {
					context.setVariable("messaggioListaVuota", "Nessun corso da visualizzare");
				}
				templateEngine.process("studente/home_studente", context, response.getWriter());
			} catch (SQLException e) {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore nel collegamento al database");
				return;
			}
		} else {
			response.sendRedirect(request.getContextPath() + "/LoginStudente");
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
