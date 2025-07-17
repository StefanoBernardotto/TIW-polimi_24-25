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

import it.polimi.tiw.beans.Corso;
import it.polimi.tiw.beans.Docente;
import it.polimi.tiw.beans.Studente;
import it.polimi.tiw.daos.CorsoDAO;
import it.polimi.tiw.misc.DatabaseInit;

/**
 * Servlet che restituisce i corsi di un docente oppure a cui è iscritto uno
 * studente
 */
@WebServlet("/Corsi")
public class Corsi extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection;

	@Override
	public void init() throws UnavailableException {
		this.connection = DatabaseInit.initDB(getServletContext());
	}

	/**
	 * Metodo di gestione della richiesta GET. Verifica che la sessione sia valida,
	 * se si, restituisce i corsi del docente o dello studente nella sessione
	 * 
	 * @param profilo : indica se la richiesta proviene dallo studente ("studente")
	 *                o dal docente ("docente")
	 */
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
		
		CorsoDAO corsoDAO = new CorsoDAO(connection);
		List<Corso> listaCorsi;
		try {
			if (profiloString.equals("docente")) {
				Docente doc = (Docente) session.getAttribute("docente");
				int codiceDocente = doc.getCodiceDocente();
				listaCorsi = corsoDAO.getCorsiByDocente(codiceDocente);
			} else {
				Studente stud = (Studente) session.getAttribute("studente");
				int matricola = stud.getMatricola();
				listaCorsi = corsoDAO.getCorsiByStudente(matricola);
			}
			
			Gson gson = new GsonBuilder().create();
			String jsonString = gson.toJson(listaCorsi);
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
