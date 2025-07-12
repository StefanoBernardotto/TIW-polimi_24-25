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

/**
 * Servlet per la pagina Esito esame dello studente
 */
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

	/**
	 * Gestione della richiesta GET. Verifica se il login è effettuato, altrimenti rimanda alla pagina di login.
	 * Se il login è valido, verifica i parametri e, se sono validi, mostra i dati relativi all'esito dell'appello selezionato (template "studente/esito").
	 * Se lo studente non è iscritto all'appello o se l'esito non è stato pubblicato mostra il relativo messaggio,
	 * altrimenti mostra i dati dell'esito e, se non verbalizzato, il bottone per rifiutarlo.
	 * @param "nome_corso" : nome del corso selezionato
	 * @param "data" : data dell'appello selezionato
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		IWebExchange webExchange = JakartaServletWebApplication.buildApplication(getServletContext())
				.buildExchange(request, response);
		WebContext context = new WebContext(webExchange);

		HttpSession session = request.getSession();
		Integer matricolaStudente = (Integer) session.getAttribute("matricola_studente");
		String nomeStudente = (String) session.getAttribute("nome_studente");
		if (session.isNew() || matricolaStudente == null || nomeStudente == null) {
			response.sendRedirect(request.getContextPath() + "/LoginStudente");
			return;
		}

		String nomeCorso = request.getParameter("nome_corso");
		context.setVariable("nome", nomeStudente);
		context.setVariable("nome_corso", nomeCorso);
		Date data;
		try {
			data = Date.valueOf(request.getParameter("data"));
		} catch (IllegalArgumentException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Formato errato");
			System.out.println(e.getStackTrace().toString());
			return;
		}
		if(nomeCorso == null || data == null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parametri mancanti");
			return;
		}

		IscrizioneDAO iscrizioneDAO = new IscrizioneDAO(connection);
		try {
			Iscrizione iscrizione = iscrizioneDAO.getDatiIscrizione(matricolaStudente, data, nomeCorso);
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
				context.setVariable("verbalizzato", iscrizione.isVerbalizzato());
			}
			templateEngine.process("studente/esito", context, response.getWriter());
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errone nella connessione al database");
			return;
		}
	}
	
	/**
	 * Gestione della richiesta POST. Verifica se il login è effettuato, altrimenti rimanda alla pagina di login.
	 * Se il login è valido e se i parametri sono validi, modifica il database e rimanda alla pagina dell'esito.
	 * @param "rifiuta_voto" : campo di verifica, deve essere sempre "rifiuta_voto"
	 * @param "nomeCorso" : nome del corso
	 * @param "dataAppello" : data dell'appello 
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		HttpSession session = request.getSession();
		Integer matricolaStudente = (Integer) session.getAttribute("matricola_studente");
		if (session.isNew() || matricolaStudente == null) {
			response.sendRedirect(request.getContextPath() + "/LoginStudente");
			return;
		}
		String check = request.getParameter("rifiuta_voto");
		String nomeCorso = request.getParameter("nomeCorso");
		Date dataAppello;
		try {
			dataAppello = Date.valueOf(request.getParameter("dataAppello"));
		}catch(IllegalArgumentException e) {
			System.out.println("Errore conversione data: " + request.getParameter("dataAppello"));
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Formato errato");
			return;
		}
		if(check.equals("rifiuta_voto") && matricolaStudente < 999999 && matricolaStudente > 100000 && nomeCorso != null && dataAppello != null) {
			IscrizioneDAO iscrizioneDAO = new IscrizioneDAO(connection);
			try {
				iscrizioneDAO.rifiutaEsito(matricolaStudente, dataAppello, nomeCorso);
				response.sendRedirect(getServletContext().getContextPath() +  "/Esito?nome_corso=" + nomeCorso + "&data=" + dataAppello.toString());
				return;
			} catch (SQLException e) {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errone nella connessione al database");
				return;
			}
		}else {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parametri mancanti");
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
