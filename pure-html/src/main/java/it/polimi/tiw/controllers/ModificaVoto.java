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
import it.polimi.tiw.misc.ComparatoreVoti;
import it.polimi.tiw.misc.DatabaseInit;
import it.polimi.tiw.misc.ThymeleafInit;

@WebServlet("/ModificaVoto")
public class ModificaVoto extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection;
	private TemplateEngine templateEngine;
	
	@Override
	public void init() throws UnavailableException {
		connection = DatabaseInit.initDB(getServletContext());
		templateEngine = ThymeleafInit.initialize(getServletContext());
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		IWebExchange webExchange = JakartaServletWebApplication.buildApplication(getServletContext())
				.buildExchange(request, response);
		WebContext context = new WebContext(webExchange);

		HttpSession session = request.getSession();
		Integer codiceDocente = (Integer) session.getAttribute("codice_docente");
		if(session.isNew() || codiceDocente == null) {
			response.sendRedirect(request.getContextPath() + "/LoginDocente");
			return;
		}
		
		String nomeCorso = request.getParameter("nome_corso");
		Date dataAppello;
		Integer matricolaStudente;
		try {
			dataAppello = Date.valueOf(request.getParameter("data_appello"));
			matricolaStudente = Integer.parseInt(request.getParameter("matricola"));
			if(matricolaStudente < 100000 || matricolaStudente > 999999)
				throw new NumberFormatException();
		}catch(NumberFormatException exception) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Formato matricola errato");
			return;
		}catch(IllegalArgumentException e){
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Formato data errato");
			return;
		}
		if(nomeCorso == null || dataAppello == null || matricolaStudente == null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parametri mancanti");
			return;
		}
		
		IscrizioneDAO iscrizioneDAO = new IscrizioneDAO(connection);
		try {
			Iscrizione iscrizione = iscrizioneDAO.getDatiIscrizione(matricolaStudente, dataAppello, nomeCorso);
			if(iscrizione != null) {
				StudenteDAO studenteDAO = new StudenteDAO(connection);
				Studente s = studenteDAO.getStudenteByMatricola(iscrizione.getMatricolaStudente());
				if (s == null) {
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Studente non trovato");
					return;
				} else {
					context.setVariable("iscrizione", iscrizione);
					context.setVariable("studente", s);
					context.setVariable("voti", ComparatoreVoti.getPossibiliVoti());
					templateEngine.process("docente/modifica_voto", context, response.getWriter());
				}
			}else {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Studente non iscritto all'appello");
				return;
			}
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore nella connessione al database");
			return;
		}
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
