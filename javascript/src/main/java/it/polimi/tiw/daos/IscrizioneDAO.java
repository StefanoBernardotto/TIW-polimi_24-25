package it.polimi.tiw.daos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import it.polimi.tiw.beans.Iscrizione;
import it.polimi.tiw.beans.Studente;
import it.polimi.tiw.beans.Verbalizzazione;
import it.polimi.tiw.misc.ComparatoreVoti;
import it.polimi.tiw.misc.Pair;

public class IscrizioneDAO {
	private Connection connection;

	public IscrizioneDAO(Connection connection) {
		this.connection = connection;
	}

	/**
	 * Metodo per ottenere i dati dell'iscrizione ad un appello di uno studente
	 * 
	 * @param matricola   : matricola dello studente
	 * @param dataAppello : data dell'appello
	 * @param nomeCorso   : nome del corso
	 * @return l'unica {@link Iscrizione} che rispetta i parametri passati,
	 *         {@code null} se non esiste
	 * @throws SQLException
	 */
	public Iscrizione getDatiIscrizione(int matricola, Date dataAppello, String nomeCorso) throws SQLException {
		String queryString = "select * from iscrizioni where matricola_studente = ? and data_appello = ? and nome_corso = ?";
		try (PreparedStatement ps = connection.prepareStatement(queryString)) {
			ps.setInt(1, matricola);
			ps.setDate(2, dataAppello);
			ps.setString(3, nomeCorso);
			try (ResultSet res = ps.executeQuery()) {
				Iscrizione iscrizione;
				if (res.isBeforeFirst()) {
					res.next();
					iscrizione = new Iscrizione(res.getString("nome_corso"), res.getDate("data_appello"),
							res.getInt("matricola_studente"), res.getString("voto"),
							res.getString("stato_pubblicazione"));
				} else {
					iscrizione = null;
				}
				return iscrizione;
			}
		}
	}

	/**
	 * Metodo per ottenere i dati dell'appello e dello studente per più studenti
	 * 
	 * @param nomeCorso      : nome del corso
	 * @param dataAppello    : data dell'appello
	 * @param listaMatricole : lista di numeri di matricola degli studenti
	 * @return una lista di tutte e sole le {@link Pair} di {@link Iscrizione} e
	 *         {@link Studente} relative all'appello del corso e degli studenti
	 *         selezionati, una lista vuota se non ci sono record che rispettano i
	 *         parametri
	 * @throws SQLException
	 */
	public List<Pair<Iscrizione, Studente>> getDatiIscrizioni(String nomeCorso, Date dataAppello,
			Integer[] listaMatricole) throws SQLException {
		String queryString = "select matricola_studente as matricola, cognome, nome, email, corso_laurea, voto, stato_pubblicazione "
				+ "from iscrizioni join studenti on iscrizioni.matricola_studente = studenti.matricola "
				+ "where iscrizioni.data_appello = ? and iscrizioni.nome_corso = ? and matricola_studente in (";
		if (listaMatricole.length > 0) {
			for (int i = 0; i < listaMatricole.length - 1; i++) {
				queryString += "?, ";
			}
			queryString += "?);";
			try (PreparedStatement ps1 = connection.prepareStatement(queryString)) {
				ps1.setDate(1, dataAppello);
				ps1.setString(2, nomeCorso);
				for (int i = 0; i < listaMatricole.length; i++) {
					ps1.setInt(i + 3, listaMatricole[i]);
				}
				try (ResultSet res = ps1.executeQuery()) {
					if (res.isBeforeFirst()) {
						List<Pair<Iscrizione, Studente>> list = new ArrayList<>();
						while (res.next()) {
							Iscrizione iscrizione = new Iscrizione(nomeCorso, dataAppello, res.getInt("matricola"),
									res.getString("voto"), res.getString("stato_pubblicazione"));
							Studente studente = new Studente(res.getInt("matricola"), res.getString("nome"),
									res.getString("cognome"), res.getString("email"), null,
									res.getString("corso_laurea"));
							list.add(new Pair<Iscrizione, Studente>(iscrizione, studente));
						}
						return list;
					} else {
						throw new SQLException("Nessuna riga ottenuta");
					}
				}
			}
		} else {
			return Collections.emptyList();
		}
	}

	/**
	 * Metodo per rifutare l'esito di un esame: in pratica imposta lo stato di
	 * valutazione a "rifiutato"
	 * 
	 * @param matricola   : matricola dello studente
	 * @param dataAppello : data dell'appello
	 * @param nomeCorso   : nome del corso
	 * @throws SQLException
	 */
	public void rifiutaEsito(int matricola, Date dataAppello, String nomeCorso) throws SQLException {
		String queryString = "update iscrizioni set stato_pubblicazione = 'rifiutato' where nome_corso = ? and data_appello = ? and matricola_studente = ?";
		try (PreparedStatement ps = connection.prepareStatement(queryString)) {
			ps.setString(1, nomeCorso);
			ps.setDate(2, dataAppello);
			ps.setInt(3, matricola);
			ps.executeUpdate();
		}
	}

	/**
	 * Metodo per ottenere i dati delle iscrizioni e degli studenti per un appello,
	 * ordinato in ordine crescente per matricola
	 * 
	 * @param dataAppello : data dell'appello
	 * @param nomeCorso   : nome del corso
	 * @return una lista di tutte e sole le {@link Pair} di {@link Iscrizione} e
	 *         {@link Studente} iscritti a quell'appello di quel corso, ordinata in
	 *         ordine crescente per matricola
	 * @throws SQLException
	 */
	public List<Pair<Iscrizione, Studente>> getOrderedIscritti(Date dataAppello, String nomeCorso) throws SQLException {
		String queryString = "select matricola_studente as matricola, cognome, nome, email, corso_laurea, voto, stato_pubblicazione "
				+ "from iscrizioni join studenti on iscrizioni.matricola_studente = studenti.matricola "
				+ "where iscrizioni.data_appello = ? and iscrizioni.nome_corso = ? " + "order by matricola";
		List<Pair<Iscrizione, Studente>> list = new ArrayList<>();
		try (PreparedStatement ps = connection.prepareStatement(queryString)) {
			ps.setDate(1, dataAppello);
			ps.setString(2, nomeCorso);
			try (ResultSet res = ps.executeQuery()) {
				if (res.isBeforeFirst()) {
					while (res.next()) {
						Iscrizione iscrizione = new Iscrizione(nomeCorso, dataAppello, res.getInt("matricola"),
								res.getString("voto"), res.getString("stato_pubblicazione"));
						Studente studente = new Studente(res.getInt("matricola"), res.getString("nome"),
								res.getString("cognome"), res.getString("email"), null, res.getString("corso_laurea"));
						list.add(new Pair<Iscrizione, Studente>(iscrizione, studente));
					}
				}
			}
		}
		return list;
	}

	/**
	 * Metodo per inserire o modificare il voto di un esame per uno studente
	 * 
	 * @param matricolaStudente : matricola dello studente
	 * @param nomeCorso         : nome del corso
	 * @param dataAppello       : data dell'appello
	 * @param nuovoVoto         : voto che si vuole inserire
	 * @throws SQLException
	 * @throws IllegalArgumentException se il voto non è nella giusta forma
	 */
	public void modificaVoto(int matricolaStudente, String nomeCorso, Date dataAppello, String nuovoVoto)
			throws SQLException, IllegalArgumentException {
		Iscrizione iscrizione = this.getDatiIscrizione(matricolaStudente, dataAppello, nomeCorso);
		if (iscrizione == null) {
			throw new SQLException("Iscrizione nulla");
		}
		if ("inserito".equals(iscrizione.getStatoPubblicazione())
				|| "non inserito".equals(iscrizione.getStatoPubblicazione())) {
			if (ComparatoreVoti.isValid(nuovoVoto)) {
				String queryString = "update iscrizioni set stato_pubblicazione = 'inserito', voto = ? where nome_corso = ? and data_appello = ? and matricola_studente = ?";
				try (PreparedStatement ps = connection.prepareStatement(queryString)) {
					ps.setString(1, nuovoVoto);
					ps.setString(2, nomeCorso);
					ps.setDate(3, dataAppello);
					ps.setInt(4, matricolaStudente);
					ps.executeUpdate();
				}
			} else {
				throw new IllegalArgumentException("Voto non valido");
			}
		} else {
			throw new SQLException("Stato di pubblicazione non valido");
		}

	}

	/**
	 * Metodo per pubblicare i voti: in pratica modifica lo stato di valutazione di
	 * tutte le iscrizioni con {@code nomeCorso} e {@code dataAppello} da
	 * {@code "inserito"} a {@code "pubblicato"}
	 * 
	 * @param nomeCorso   : nome del corso
	 * @param dataAppello : data dell'appello
	 * @throws SQLException
	 */
	public void pubblicaVoti(String nomeCorso, Date dataAppello) throws SQLException {
		String queryString = "update iscrizioni set stato_pubblicazione = 'pubblicato' where stato_pubblicazione = 'inserito' and nome_corso = ? and data_appello = ?;";
		try (PreparedStatement ps = connection.prepareStatement(queryString)) {
			ps.setString(1, nomeCorso);
			ps.setDate(2, dataAppello);
			ps.executeUpdate();
		}
	}

	/**
	 * Metodo per verbalizzare i voti: in pratica modifica lo stato di valutazione
	 * di tutte le iscrizioni con {@code nomeCorso} e {@code dataAppello} da
	 * {@code "pubblicato} o {@code "rifiutato"} a {@code "verbalizzato"}, nel caso
	 * lo stato fosse {@code "rifiutato"} il voto viene modificato in
	 * {@code "rimandato"}. Infine crea un {@link Verbale} e inserisce la relazione
	 * tra {@link Verbale} e {@link Appello} nel database.
	 * 
	 * @param nomeCorso   : nome del corso
	 * @param dataAppello data dell'appello
	 * @return il codice {@link UUID} del verbale generato, in formato stringa
	 * @throws SQLException
	 */
	public String verbalizzaVoti(String nomeCorso, Date dataAppello) throws SQLException {
		UUID codiceVerbale = null;
		String codiceVerbaleString;
		// Disabilitazione dell'autocommit
		connection.setAutoCommit(false);
		// Select di tutte le righe interessate dalla modifica
		String querySelectIscrizioni = "select * from iscrizioni where nome_corso = ? and data_appello = ? and (stato_pubblicazione = 'pubblicato' or stato_pubblicazione = 'rifiutato');";
		List<Verbalizzazione> listaVerbalizzazioni = new ArrayList<>();
		try (PreparedStatement ps0 = connection.prepareStatement(querySelectIscrizioni)) {
			ps0.setString(1, nomeCorso);
			ps0.setDate(2, dataAppello);
			try (ResultSet res1 = ps0.executeQuery()) {
				if (res1.isBeforeFirst()) {
					codiceVerbale = UUID.randomUUID();
					codiceVerbaleString = codiceVerbale.toString();
					while (res1.next()) {
						listaVerbalizzazioni.add(new Verbalizzazione(codiceVerbale, res1.getInt("matricola_studente")));
					}
					// Modifica del voto a "rimandato" per tutte le righe in stato "rifiutato"
					String queryUpdateVoti = "update iscrizioni set voto = 'rimandato' where nome_corso = ? and data_appello = ? and stato_pubblicazione = 'rifiutato';";
					try (PreparedStatement ps1 = connection.prepareStatement(queryUpdateVoti)) {
						ps1.setString(1, nomeCorso);
						ps1.setDate(2, dataAppello);
						ps1.executeUpdate();
						// Modifica dello stato di valutazione a "verbalizzato" per le righe interessate
						String queryUpdateIscrizioni = "update iscrizioni set stato_pubblicazione = 'verbalizzato' where nome_corso = ? and data_appello = ? and (stato_pubblicazione = 'pubblicato' or stato_pubblicazione = 'rifiutato');";
						try (PreparedStatement ps2 = connection.prepareStatement(queryUpdateIscrizioni)) {
							ps2.setString(1, nomeCorso);
							ps2.setDate(2, dataAppello);
							if (ps2.executeUpdate() != listaVerbalizzazioni.size()) {
								throw new SQLException("Nessuna riga modificata");
							}
							// Creazione e inserimento del verbale
							String queryInsertVerbale = "insert into verbali (codice, data_creazione, ora_creazione, nome_corso, data_appello) values (?, CURRENT_DATE, CURRENT_TIME, ?, ?)";
							try (PreparedStatement ps3 = connection.prepareStatement(queryInsertVerbale)) {
								ps3.setString(1, codiceVerbaleString);
								ps3.setString(2, nomeCorso);
								ps3.setDate(3, dataAppello);
								if (ps3.executeUpdate() != 1) {
									throw new SQLException("Errore d'inserimento");
								}
								// Inserimento delle relazioni verbale - studente
								String queryInsertVerbalizzazioni = "insert into verbalizzazioni (codice_verbale, matricola_studente) values (?, ?);";
								for (Verbalizzazione verbalizzazione : listaVerbalizzazioni) {
									try (PreparedStatement ps4 = connection
											.prepareStatement(queryInsertVerbalizzazioni)) {
										ps4.setString(1, codiceVerbaleString);
										ps4.setInt(2, verbalizzazione.getMatricolaStudente());
										if (ps4.executeUpdate() != 1) {
											throw new SQLException("Errore d'inserimento");
										}
									}
								}
							}
						}
					}
					// Commit della transazione e riabilitazione dell'autocommit
					connection.commit();
					connection.setAutoCommit(true);
					return codiceVerbaleString;
				} else {
					connection.rollback();
					connection.setAutoCommit(true);
					return "no-rows";
				}
			}
		} catch (SQLException e) {
			connection.rollback();
			connection.setAutoCommit(true);
			throw e;
		}
	}

}