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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import it.polimi.tiw.beans.Iscrizione;
import it.polimi.tiw.beans.Studente;
import it.polimi.tiw.daos.IscrizioneDAO;
import it.polimi.tiw.misc.DatabaseInit;
import it.polimi.tiw.misc.Pair;

/**
 * Servlet per la gestione delle richieste relative agli iscritti ad un appello.
 * Permette al docente di visualizzare gli iscritti con i relativi voti, ma
 * anche di pubblicare o verbalizzare i voti
 */
@WebServlet("/Iscritti")
public class Iscritti extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection;

	@Override
	public void init() throws UnavailableException {
		this.connection = DatabaseInit.initDB(getServletContext());
	}

	/**
	 * Servlet per la gestione di richieste GET. Verifica che la sessione sia
	 * valida, se sì, invia al client gli iscritti all'appello richiesto
	 * 
	 * @param nome_corso   : nome del corso
	 * @param data_appello : data dell'appello del corso di cui si vuole
	 *                     visualizzare gli iscritti
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		if (session.isNew() || session.getAttribute("docente") == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		String nomeCorso = request.getParameter("nome_corso");
		Date dataAppello;
		try {
			java.util.Date tmpDate = new SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("data_appello"));
			dataAppello = new Date(tmpDate.getTime());
		} catch (ParseException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Formato data errato");
			return;
		}
		if (nomeCorso == null || dataAppello == null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Parametri mancanti");
			return;
		}

		IscrizioneDAO iscrizioneDAO = new IscrizioneDAO(connection);
		List<Pair<Iscrizione, Studente>> listaIscritti;
		try {
			listaIscritti = iscrizioneDAO.getOrderedIscritti(dataAppello, nomeCorso);

			Gson gson = new GsonBuilder().create();
			String jsonString = gson.toJson(listaIscritti);
			response.setStatus(HttpServletResponse.SC_OK);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonString);
		} catch (SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("Errore nel server");
			return;
		}
	}

	/**
	 * Metodo per la gestione delle richieste POST. Verifica che la sessione sia
	 * valida, se sì, pubblica o verbalizza (in base al parametro azione) i voti
	 * nello stato opportuno
	 * 
	 * @param nome_corso   : nome del corso di cui si vuole pubblicare o
	 *                     verbalizzare dei voti
	 * @param data_appello : data dell'appello a cui si fa riferimento
	 * @param azione       : "pubblica" o "verbalizza"
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		if (session.isNew() || session.getAttribute("docente") == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		String nomeCorso = request.getParameter("nome_corso");
		String azione = request.getParameter("azione");
		Date dataAppello;
		try {
			java.util.Date tmpDate = new SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("data_appello"));
			dataAppello = new Date(tmpDate.getTime());
		} catch (ParseException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Formato data errato");
			return;
		}

		if (nomeCorso == null || dataAppello == null || azione == null
				|| (!azione.equals("verbalizza") && !azione.equals("pubblica"))) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Parametri mancanti");
			return;
		}

		IscrizioneDAO iscrizioneDAO = new IscrizioneDAO(connection);
		try {
			if (azione.equals("pubblica")) {
				iscrizioneDAO.pubblicaVoti(nomeCorso, dataAppello);
				response.setStatus(HttpServletResponse.SC_OK);
			} else if (azione.equals("verbalizza")) {
				String codiceVerbaleString = iscrizioneDAO.verbalizzaVoti(nomeCorso, dataAppello);
				if ("no-rows".equals(codiceVerbaleString)) {
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					response.getWriter().println("Niente da verbalizzare");
				} else {
					response.setStatus(HttpServletResponse.SC_OK);
					response.getWriter().println(codiceVerbaleString);
				}
			}
		} catch (SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("Errore nel server");
			return;
		}
	}

}
