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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import it.polimi.tiw.beans.Appello;
import it.polimi.tiw.daos.AppelloDAO;
import it.polimi.tiw.misc.DatabaseInit;

/**
 * Servlet per ottenere le date di appello di un corso
 */
@WebServlet("/Appelli")
public class Appelli extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection;

	@Override
	public void init() throws UnavailableException {
		this.connection = DatabaseInit.initDB(getServletContext());
	}

	/**
	 * Metodo per la gestione di richieste GET. Verifica che la sessione sia valida,
	 * se sì, restituisce le date d'appello per il corso selezionato
	 * 
	 * @param nome_corso : nome del corso di cui si vogliono ottenere le date
	 * @param profilo    : indica se la richiesta proviene dallo studente
	 *                   ("studente") o dal docente ("docente")
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String profiloString = request.getParameter("profilo");
		if (!"studente".equals(profiloString) && !"docente".equals(profiloString)) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Parametri mancanti");
			return;
		}
		
		HttpSession session = request.getSession();
		if (session.isNew() || session.getAttribute(profiloString) == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}
		
		String nomeCorso = request.getParameter("nome_corso");
		if(nomeCorso == null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Parametri mancanti");
			return;
		}
		
		AppelloDAO appelloDAO = new AppelloDAO(connection);
		List<Appello> listaAppelli;
		try {
			listaAppelli = appelloDAO.getAppelliByCorso(nomeCorso);
			
			Gson gson = new GsonBuilder().create();
			String jsonString = gson.toJson(listaAppelli);
			response.setStatus(HttpServletResponse.SC_OK);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonString);
		}catch (SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("Errore nel server");
			return;
		}
	}

	@Override
	public void destroy() {
		try {
			if(!this.connection.isClosed()) {
				this.connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
