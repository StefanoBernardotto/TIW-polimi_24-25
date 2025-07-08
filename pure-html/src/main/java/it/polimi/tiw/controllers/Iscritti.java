package it.polimi.tiw.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.UnavailableException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.util.List;
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
import it.polimi.tiw.misc.DatabaseInit;
import it.polimi.tiw.misc.Pair;
import it.polimi.tiw.misc.ThymeleafInit;

@WebServlet("/Iscritti")
public class Iscritti extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection;
	private TemplateEngine templateEngine;
	
	@Override
	public void init() throws UnavailableException {
		connection = DatabaseInit.initDB(getServletContext());
		templateEngine = ThymeleafInit.initialize(getServletContext());
	}
	
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
		try {
			dataAppello = Date.valueOf(request.getParameter("data_appello"));
		}catch(IllegalArgumentException exception) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Formato data errato");
			return;
		}
		String campoOrdine = request.getParameter("campo_ordine");
		if(nomeCorso == null || (!campoOrdine.equals("matricola") && !campoOrdine.equals("cognome") && 
				!campoOrdine.equals("nome") && !campoOrdine.equals("email") && !campoOrdine.equals("corso_laurea") && 
				!campoOrdine.equals("voto") && !campoOrdine.equals("stato_pubblicazione"))) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Formato parametri errato");
			return;
		}	
		IscrizioneDAO iscrizioneDAO = new IscrizioneDAO(connection);
		try {
			List<Pair<Iscrizione, Studente>> listaIscritti = iscrizioneDAO.getOrderedIscrittiByAppello(dataAppello, nomeCorso, campoOrdine, true);
			System.out.println(listaIscritti);
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore nella connessione al database");
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
