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
 * Permette al docente di visualizzare gli iscritti con i relativi voti, 
 * ma anche di pubblicare o verbalizzare i voti
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
     * Servlet per la gestione di richieste GET. Verifica che la sessione sia valida,
     * se sì, invia al client gli iscritti all'appello richiesto
     * 
     * @param nome_corso : nome del corso
     * @param data_appello : data dell'appello del corso di cui si vuole visualizzare gli iscritti
     */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		if (session.isNew() || session.getAttribute("docente") == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}
		
		String nomeCorso = request.getParameter("nome_corso");
		Date dataAppello;
		try {
			dataAppello = Date.valueOf(request.getParameter("data_appello"));
		} catch (IllegalArgumentException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Formato data errato");
			return;
		}
		if(nomeCorso == null || dataAppello == null) {
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
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
