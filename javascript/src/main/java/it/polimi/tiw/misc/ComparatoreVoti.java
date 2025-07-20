package it.polimi.tiw.misc;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe di utilità che contiene la logica di ordinamento dei voti e permette di verificare la validità di un voto
 */
public class ComparatoreVoti{
	private static final List<String> list = new ArrayList<>(32);
	
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
	 * Metodo per verificare la validità di un voto
	 * @param voto : voto da verificare
	 * @return {@code true} se il voto è valido, {@code false} altrimenti
	 */
	public static boolean isValid(String voto) {
		return (voto != null && voto != "" && list.contains(voto));
	}
}