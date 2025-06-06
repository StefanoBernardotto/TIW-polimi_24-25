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
import java.sql.Date;
import java.sql.SQLException;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import it.polimi.tiw.beans.Iscrizione;
import it.polimi.tiw.beans.Studente;
import it.polimi.tiw.daos.IscrizioneDAO;
import it.polimi.tiw.daos.StudenteDAO;
import it.polimi.tiw.misc.DatabaseInit;
import it.polimi.tiw.misc.ThymeleafInit;

@WebServlet("/Esito")
public class Esito extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection;
	private TemplateEngine templateEngine;

	@Override
	public void init() throws UnavailableException {
		connection = DatabaseInit.initDB(getServletContext());
		templateEngine = ThymeleafInit.initialize(getServletContext());
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		IWebExchange webExchange = JakartaServletWebApplication.buildApplication(getServletContext())
				.buildExchange(request, response);
		WebContext context = new WebContext(webExchange);

		HttpSession session = request.getSession();
		Integer matricolaStudente = (Integer) session.getAttribute("matricola_studente");
		if (session.isNew() || matricolaStudente == null) {
			response.sendRedirect(request.getContextPath() + "/LoginStudente");
			return;
		}

		String nomeCorso = request.getParameter("nome_corso");
		Date data;
		try {
			data = Date.valueOf(request.getParameter("data"));
		} catch (IllegalArgumentException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Formato errato");
			return;
		}

		IscrizioneDAO iscrizioneDAO = new IscrizioneDAO(connection);
		try {
			Iscrizione iscrizione = iscrizioneDAO.getEsitoEsame(matricolaStudente, data, nomeCorso);
			if (iscrizione == null) {
				context.setVariable("messaggioNoEsito", "Non iscritto a questo appello");
			} else if (!iscrizione.isPubblicato()) {
				context.setVariable("messaggioNoEsito", "Esito non ancora pubblicato");
			} else {
				StudenteDAO studenteDAO = new StudenteDAO(connection);
				Studente s = studenteDAO.getStudenteByMatricola(iscrizione.getMatricolaStudente());
				if (s == null) {
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Studente non trovato");
					return;
				} else {
					context.setVariable("studente", s);
				}
				context.setVariable("iscrizione", iscrizione);
				context.setVariable("rifiutato", iscrizione.isRifiutato());
			}
			templateEngine.process("studente/esito", context, response.getWriter());
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errone nella connessione al database");
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
