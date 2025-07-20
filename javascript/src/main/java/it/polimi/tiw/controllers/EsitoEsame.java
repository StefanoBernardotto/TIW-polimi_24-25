package it.polimi.tiw.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.UnavailableException;
import jakarta.servlet.annotation.MultipartConfig;
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
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import it.polimi.tiw.beans.Iscrizione;
import it.polimi.tiw.beans.Studente;
import it.polimi.tiw.daos.IscrizioneDAO;
import it.polimi.tiw.daos.StudenteDAO;
import it.polimi.tiw.misc.ComparatoreVoti;
import it.polimi.tiw.misc.DatabaseInit;
import it.polimi.tiw.misc.Pair;

/**
 * Servlet implementation class EsitoEsame
 */
@WebServlet("/EsitoEsame")
@MultipartConfig
public class EsitoEsame extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection;

	@Override
	public void init() throws UnavailableException {
		this.connection = DatabaseInit.initDB(getServletContext());
	}

	/**
	 * Gestione delle richieste GET. In base al profilo specificato verifica se la
	 * sessione è valida, se sì permette di ottenere i dati relativi all'appello del
	 * corso passato per lo studente specificato (o che effettua la richiesta)
	 * 
	 * @param profilo      : deve essere "studente" o "docente"
	 * @param nome_corso   : nome del corso
	 * @param data_appello : data dell'appello richiesto
	 * @param matricola    : solo per il docente, permette di selezionare lo
	 *                     studente di cui avere i dati
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

		Integer matricola;
		if (profiloString.equals("studente")) {
			Studente studente = (Studente) session.getAttribute("studente");
			matricola = studente.getMatricola();
		} else {
			try {
				matricola = Integer.parseInt(request.getParameter("matricola"));
				if (matricola < 100000 || matricola > 999999) {
					throw new NumberFormatException();
				}
			} catch (NumberFormatException e) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().println("Formato matricola errato");
				return;
			}
		}

		Iscrizione iscrizione;
		try {
			IscrizioneDAO iscrizioneDAO = new IscrizioneDAO(connection);
			iscrizione = iscrizioneDAO.getDatiIscrizione(matricola, dataAppello, nomeCorso);
		} catch (SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("Errore nel server");
			return;
		}

		Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
		String jsonString;
		if (profiloString.equals("docente")) {
			StudenteDAO studenteDAO = new StudenteDAO(connection);
			try {
				Studente studente = studenteDAO.getStudenteByMatricola(matricola);
				Pair<Iscrizione, Studente> pair = new Pair<Iscrizione, Studente>(iscrizione, studente);
				jsonString = gson.toJson(pair);
			} catch (SQLException e) {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				response.getWriter().println("Errore nel server");
				return;
			}
		} else {
			jsonString = gson.toJson(iscrizione);
		}
		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(jsonString);
	}

	/**
	 * Gestione delle richieste POST. In base al profilo specificato verifica se la
	 * sessione è valida, se sì permette di rifiutare un esito (azione=rifiuta)
	 * oppure modificare il voto (azione=modifica)
	 * 
	 * @param profilo      : deve essere "studente" o "docente"
	 * @param azione       : deve essere "modifica" o "rifiuta"
	 * @param nome_corso   : nome del corso
	 * @param data_appello : data dell'appello richiesto
	 * @param matricola    : solo per il docente, permette di selezionare lo
	 *                     studente di cui modificare il voto
	 * @param voto         : voto da assegnare allo studente
	 * @param multi_voto   : json contenete le coppie {@code matricola, voto}
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
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

		String azione = request.getParameter("azione");
		if ((!"rifiuta".equals(azione) && profiloString.equals("studente"))
				|| (!"modifica".equals(azione) && profiloString.equals("docente"))) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Parametri errati");
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

		if (azione.equals("rifiuta") && profiloString.equals("studente")) {
			IscrizioneDAO iscrizioneDAO = new IscrizioneDAO(connection);
			Studente studente = (Studente) session.getAttribute("studente");
			try {
				iscrizioneDAO.rifiutaEsito(studente.getMatricola(), dataAppello, nomeCorso);
				response.setStatus(HttpServletResponse.SC_OK);
			} catch (SQLException e) {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				response.getWriter().println("Errore nel server");
				return;
			}
		} else if (azione.equals("modifica") && profiloString.equals("docente")) {
			Integer matricola;
			try {
				matricola = Integer.parseInt(request.getParameter("matricola"));
				if (matricola != -1 && (matricola < 100000 || matricola > 999999)) {
					throw new NumberFormatException();
				}
			} catch (NumberFormatException e) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().println("Formato matricola errato");
				return;
			}
			if (matricola != -1) {
				// Inserimento singolo
				String voto = request.getParameter("voto");
				if (!ComparatoreVoti.isValid(voto)) {
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					response.getWriter().println("Voto non valido");
					return;
				}
				IscrizioneDAO iscrizioneDAO = new IscrizioneDAO(connection);
				try {
					iscrizioneDAO.modificaVoto(matricola, nomeCorso, dataAppello, voto);
				} catch (SQLException e) {
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					response.getWriter().println("Errore nel server");
					return;
				} catch (IllegalArgumentException e) {
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					response.getWriter().println("Voto non valido");
					return;
				}
			} else {
				// Inserimento multiplo
				try {
					JsonArray array = (JsonArray) JsonParser.parseString(request.getParameter("multi_voto"));
					Map<Integer, String> mappaVoti = new HashMap<Integer, String>();
					array.forEach(coppia -> {
						Integer tmpMat = coppia.getAsJsonObject().get("matricola").getAsInt();
						String tmpVoto = coppia.getAsJsonObject().get("voto").getAsString();
						if (tmpMat > 100000 && tmpMat < 999999 && ComparatoreVoti.isValid(tmpVoto))
							mappaVoti.put(tmpMat, tmpVoto);
						else
							throw new IllegalArgumentException();
					});
					IscrizioneDAO iscrizioneDAO = new IscrizioneDAO(connection);
					iscrizioneDAO.modificaMultipliVoti(nomeCorso, dataAppello, mappaVoti);
				} catch (JsonParseException e) {
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					response.getWriter().println("Errore nel json multivoto");
					return;
				} catch (SQLException e) {
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					response.getWriter().println("Errore nel server");
					return;
				} catch (IllegalArgumentException e) {
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					response.getWriter().println("Parametri non validi");
					return;
				}

			}
			response.setStatus(HttpServletResponse.SC_OK);
		}
	}

}
