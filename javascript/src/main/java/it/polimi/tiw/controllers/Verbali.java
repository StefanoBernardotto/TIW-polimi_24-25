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
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import it.polimi.tiw.beans.Docente;
import it.polimi.tiw.beans.Iscrizione;
import it.polimi.tiw.beans.Studente;
import it.polimi.tiw.beans.Verbale;
import it.polimi.tiw.daos.IscrizioneDAO;
import it.polimi.tiw.daos.VerbaleDAO;
import it.polimi.tiw.misc.DatabaseInit;
import it.polimi.tiw.misc.Pair;

/**
 * Servlet per la gestione dei verbali: permette al docente di ottenere l'elenco
 * dei verbali e i dati completi di un verbale
 */
@WebServlet("/Verbali")
public class Verbali extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection;

	@Override
	public void init() throws UnavailableException {
		this.connection = DatabaseInit.initDB(getServletContext());
	}

	/**
	 * Gestione delle richieste GET. Verifica che la sessione sia valida, se sì
	 * restituisce tutti i verbali del docente (nessun parametro) oppure il verbale
	 * corrispondente al codice passato per parametro
	 * 
	 * @param codice : codice del verbale che si vuole ottenere, {@code null} se si
	 *               vuole avere l'elenco dei verbali
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		if (session.isNew() || session.getAttribute("docente") == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		String codiceVerbaleString = request.getParameter("codice");
		UUID codiceVerbale = null;
		if (codiceVerbaleString != null) {
			try {
				codiceVerbale = UUID.fromString(codiceVerbaleString);
			} catch (IllegalArgumentException e) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().println("Formato codice non valido");
				return;
			}
		}

		Gson gson = new GsonBuilder()
				.setDateFormat("dd/MM/yyyy").create();
		String jsonString = "";
		try {
			if (codiceVerbale != null) {
				VerbaleDAO verbaleDAO = new VerbaleDAO(connection);
				Verbale verbale = verbaleDAO.getVerbaleByCodice(codiceVerbale);
				IscrizioneDAO iscrizioneDAO = new IscrizioneDAO(connection);
				Integer[] matricoleVerbalizzate = verbaleDAO.getMatricoleByCodice(codiceVerbale);
				List<Pair<Iscrizione, Studente>> listaVerbalizzatiPair = iscrizioneDAO
						.getDatiIscrizioni(verbale.getNomeCorso(), verbale.getDataAppello(), matricoleVerbalizzate);
				Pair<Verbale, List<Pair<Iscrizione, Studente>>> pair = 
						new Pair<Verbale, List<Pair<Iscrizione,Studente>>>(verbale, listaVerbalizzatiPair);
				jsonString = gson.toJson(pair);
			}else {
				VerbaleDAO verbaleDAO = new VerbaleDAO(connection);
				Docente docente = (Docente) session.getAttribute("docente");
				List<Verbale> listaVerbali = verbaleDAO.getOrderdVerbaliByDocente(docente.getCodiceDocente());
				jsonString = gson.toJson(listaVerbali);
			}
			
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

}
