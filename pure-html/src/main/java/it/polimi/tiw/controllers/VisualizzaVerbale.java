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
import java.sql.SQLException;
import java.util.UUID;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import it.polimi.tiw.beans.Iscrizione;
import it.polimi.tiw.beans.Studente;
import it.polimi.tiw.beans.Verbale;
import it.polimi.tiw.daos.IscrizioneDAO;
import it.polimi.tiw.daos.VerbaleDAO;
import it.polimi.tiw.misc.DatabaseInit;
import it.polimi.tiw.misc.Logger;
import it.polimi.tiw.misc.Pair;
import it.polimi.tiw.misc.ThymeleafInit;

/**
 * Servlet per la pagina di visualizzazione di un verbale
 */
@WebServlet("/Verbale")
public class VisualizzaVerbale extends HttpServlet {
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
	 * Se il login è valido e se i parametri sono validi, mostra i dati del verbale selezionato e tutti gli 
	 * studenti con le informazioni sul voto (template "docente/verbale")
	 * @param "codice_verbale" : codice del verbale selezionato
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		IWebExchange webExchange = JakartaServletWebApplication.buildApplication(getServletContext())
				.buildExchange(request, response);
		WebContext context = new WebContext(webExchange);

		HttpSession session = request.getSession();
		Integer codiceDocente = (Integer) session.getAttribute("codice_docente");
		if (session.isNew() || codiceDocente == null) {
			response.sendRedirect(request.getContextPath() + "/LoginDocente");
			return;
		}
		
		UUID codiceVerbale;
		try {
			String codiceVerbaleString = request.getParameter("codice_verbale");
			if(codiceVerbaleString == null) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parametri mancanti");
				return;
			}
			codiceVerbale = UUID.fromString(codiceVerbaleString);
		}catch(IllegalArgumentException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Formato parametri errato");
			return;
		}
		
		VerbaleDAO verbaleDAO = new VerbaleDAO(connection);
		try {
			Verbale verbale = verbaleDAO.getVerbaleByCodice(codiceVerbale);
			IscrizioneDAO iscrizioneDAO = new IscrizioneDAO(connection);
			Integer[] matricoleVerbalizzate = verbaleDAO.getMatricoleByCodice(codiceVerbale);
			List<Pair<Iscrizione, Studente>> listaVerbalizzatiPair = iscrizioneDAO.getDatiIscrizioni(verbale.getNomeCorso(), verbale.getDataAppello(), matricoleVerbalizzate);
			context.setVariable("listaVerbalizzatiPair", listaVerbalizzatiPair);
			context.setVariable("verbale", verbale);
			templateEngine.process("docente/verbale", context, response.getWriter());
		} catch (SQLException e) {
			Logger.info("Error: " + e.getMessage());
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore nella connessione al database");
			return;
		}
	}

}
