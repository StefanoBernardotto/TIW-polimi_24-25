package it.polimi.tiw.misc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.polimi.tiw.beans.Iscrizione;
import it.polimi.tiw.beans.Studente;

public class ComparatoreVoti implements Comparator<Pair<Iscrizione, Studente>>{
	private static final List<String> list = new ArrayList<>(32);
	private boolean decrescente = false;
	
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
	
	public static List<String> getPossibiliVoti(){
		List<String> tmpList = new ArrayList<String>(list);
		tmpList.remove("<vuoto>");
		for(int i = 4; i < 18; i++) {
			tmpList.remove("");
		}
		return tmpList;
	}
	
	public static boolean isValid(String voto) {
		return (voto != null && voto != "" && list.contains(voto));
	}
	
	public ComparatoreVoti(boolean decrescente) {
		this.decrescente = decrescente;
	}

	@Override
	public int compare(Pair<Iscrizione, Studente> p1, Pair<Iscrizione, Studente> p2) {
		String voto1 = p1.getFirst().getVoto(), voto2 = p2.getFirst().getVoto();
		Integer valoreVoto1 = list.indexOf(voto1), valoreVoto2 = list.indexOf(voto2);
		if(decrescente)
			return valoreVoto2.compareTo(valoreVoto1);
		return valoreVoto1.compareTo(valoreVoto2);
	}
	
	
	
}