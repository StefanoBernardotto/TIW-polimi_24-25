package it.polimi.tiw.misc;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import it.polimi.tiw.beans.Iscrizione;
import it.polimi.tiw.beans.Studente;

/**
 * Classe di utilità che contiene la logica di ordinamento dei voti e permette di verificare la validità di un voto
 */
public class ComparatoreVoti implements Comparator<Pair<Iscrizione, Studente>>{
	private static final List<String> list = new ArrayList<>(32);
	private boolean decrescente = false;
	
	// Istruzioni eseguite una sola volta: inserisce i voti in una lista secondo l'ordine fissato dalla specifica
	static {
		list.add(0, "<vuoto>");
		list.add(1, "assente");
		list.add(2, "rimandato");
		list.add(3, "riprovato");
		for(int i = 4; i < 18; i++) {
			list.add(i, "");
		}
		for(Integer i = 18; i <= 30; i++) {
			list.add(i, i.toString());
		}
		list.add(31, "30 e lode");
	}
	
	/**
	 * Metodo per ottenere la lista dei voti assegnabili
	 * @return una lista di stringhe rappresentanti i voti assegnabili
	 */
	public static List<String> getPossibiliVoti(){
		List<String> tmpList = new ArrayList<String>(list);
		tmpList.remove("<vuoto>");
		for(int i = 4; i < 18; i++) {
			tmpList.remove("");
		}
		return tmpList;
	}
	
	/**
	 * Metodo per verificare la validità di un voto
	 * @param voto : voto da verificare
	 * @return {@code true} se il voto è valido, {@code false} altrimenti
	 */
	public static boolean isValid(String voto) {
		return (voto != null && voto != "" && list.contains(voto));
	}
	
	public ComparatoreVoti(boolean decrescente) {
		this.decrescente = decrescente;
	}

	
	/**
	 * Metodo per comparare due {@link Pair} di {@link Iscrizione} e {@link Studente} secondo il voto, nell'ordine richiesto da specifica. 
	 * La logica di ordinamento crescente o decrescente è decisa in base al campo {@code decrescente} del comparatore, da specificare in fase di istanziazione
	 * @param p1, p2 : le due coppie da comparare
	 * @return la distanza tra i due indici dei voti
	 */
	@Override
	public int compare(Pair<Iscrizione, Studente> p1, Pair<Iscrizione, Studente> p2) {
		String voto1 = p1.getFirst().getVoto(), voto2 = p2.getFirst().getVoto();
		Integer valoreVoto1 = list.indexOf(voto1), valoreVoto2 = list.indexOf(voto2);
		if(decrescente)
			return valoreVoto2.compareTo(valoreVoto1);
		return valoreVoto1.compareTo(valoreVoto2);
	}
	
	
	
}