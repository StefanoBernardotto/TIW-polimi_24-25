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

/**
 * Servlet per la pagina Iscritti del docente
 */
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

	/**
	 * Gestione della richiesta GET. Verifica se il login è effettuato, altrimenti rimanda alla pagina di login.
	 * Se il login è valido e se i parametri sono validi, mostra gli iscritti all'appello selezionato (template "docente/iscritti")
	 * @param "nomeCorso" : nome del corso
	 * @param "dataAppello" : data dell'appello
	 * @param "campo_ordine" : campo secondo cui si vuole ordinare, sempre a "start" per il primo accesso alla pagina (non dovuto a click sull'intestazione)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		IWebExchange webExchange = JakartaServletWebApplication.buildApplication(getServletContext())
				.buildExchange(request, response);
		WebContext context = new WebContext(webExchange);

		HttpSession session = request.getSession();
		Integer codiceDocente = (Integer) session.getAttribute("codice_docente");
		if (session.isNew() || codiceDocente == null) {
			response.sendRedirect(request.getContextPath() + "/LoginDocente");
			return;
		}

		String nomeCorso = request.getParameter("nome_corso");
		Date dataAppello;
		try {
			dataAppello = Date.valueOf(request.getParameter("data_appello"));
		} catch (IllegalArgumentException exception) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Formato data errato");
			return;
		}
		String campoOrdine = request.getParameter("campo_ordine");
		if (nomeCorso == null || (!campoOrdine.equals("start") && !campoOrdine.equals("matricola")
				&& !campoOrdine.equals("cognome") && !campoOrdine.equals("nome") && !campoOrdine.equals("email")
				&& !campoOrdine.equals("corso_laurea") && !campoOrdine.equals("voto")
				&& !campoOrdine.equals("stato_pubblicazione"))) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Formato parametri errato");
			return;
		}
		// Nella sessione viene salvato il campo di ordinamento che verrà utilizzato alla successiva richiesta (dovuta al click su un'intestazione) 
		String oldCampoOrdine = (String) session.getAttribute("old_campo_ordine");
		Boolean ord;
		if (campoOrdine.equals("start")) {
			// Primo accesso alla pagina: ordinamento crescente in base alla matricola
			campoOrdine = new String("matricola");
			session.setAttribute("old_campo_ordine", campoOrdine);
			session.setAttribute("ordinamento", false);
			ord = false;
		} else if (campoOrdine.equals(oldCampoOrdine)) {
			// Ulteriore accesso alla pagina con stesso campo di ordinamento precedente: inverte l'ordine
			ord = !(Boolean) session.getAttribute("ordinamento");
			session.setAttribute("ordinamento", ord);
		} else {
			// Accesso alla pagina con campo ordine diverso dal precedente: ordinamento crescente in base a quel campo
			session.setAttribute("old_campo_ordine", campoOrdine);
			session.setAttribute("ordinamento", false);
			ord = false;
		}
		context.setVariable("nomeCorso", nomeCorso);
		context.setVariable("dataAppello", dataAppello.toString());
		IscrizioneDAO iscrizioneDAO = new IscrizioneDAO(connection);
		try {
			List<Pair<Iscrizione, Studente>> listaIscritti = iscrizioneDAO.getOrderedIscritti(dataAppello,
					nomeCorso, campoOrdine, ord);
			context.setVariable("listaIscritti", listaIscritti);
			templateEngine.process("docente/iscritti", context, response.getWriter());
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore nella connessione al database");
			return;
		}
	}

	/**
	 * Gestione della richiesta POST. Verifica se il login è effettuato, altrimenti rimanda alla pagina di login.
	 * Se il login è valido e se i parametri sono validi, esegue la pubblicazione o la verbalizzazione dei voti.
	 * Nel caso di pubblicazione, viene mostrata la pagina Iscritti, altrimenti viene mostrato il verbale creato.
	 * @param "nomeCorso" : nome del corso
	 * @param "dataAppello" : data dell'appello
	 * @param "azione" : azione da eseguire, deve essere necessariamente "pubblica" o "verbalizza"
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		Integer codiceDocente = (Integer) session.getAttribute("codice_docente");
		if (session.isNew() || codiceDocente == null) {
			response.sendRedirect(request.getContextPath() + "/LoginDocente");
			return;
		}

		String nomeCorso = request.getParameter("nome_corso");
		String azione = request.getParameter("azione");
		Date dataAppello;
		try {
			dataAppello = Date.valueOf(request.getParameter("data_appello"));
		} catch (IllegalArgumentException exception) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Formato data errato");
			return;
		}

		if (nomeCorso == null || dataAppello == null || azione == null
				|| (!azione.equals("verbalizza") && !azione.equals("pubblica"))) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parametri errati");
			return;
		}

		IscrizioneDAO iscrizioneDAO = new IscrizioneDAO(connection);
		try {
			if (azione.equals("pubblica")) {
				iscrizioneDAO.pubblicaVoti(nomeCorso, dataAppello);
				response.sendRedirect(request.getContextPath() + "/Iscritti?nome_corso=" + nomeCorso + "&data_appello=" + dataAppello + "&campo_ordine=start");
			} else if (azione.equals("verbalizza")) {
				String codiceVerbaleString = iscrizioneDAO.verbalizzaVoti(nomeCorso, dataAppello);
				if("no-rows".equals(codiceVerbaleString)) {
					response.sendRedirect(request.getContextPath() + "/Iscritti?nome_corso=" + nomeCorso + "&data_appello=" + dataAppello.toString() + "&campo_ordine=start");
					return;
				}else {
					response.sendRedirect(request.getContextPath() + "/Verbale?codice_verbale=" + codiceVerbaleString);
					return;
				}
			}
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore nella connessione al database");
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
