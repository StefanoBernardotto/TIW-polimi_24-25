/**
 * funzioni di utilità
 */

/**
 * Funzione utilizzata per effettuare le chiamate asincrone al server
 * @param method : metodo della richiesta Http
 * @param url : url della richiesta Http
 * @param data : formData da inviare nella richiesta
 * @param callback : funzione da eseguire una volta ricevuta la risposta
 */
function makeCall(method, url, data, callback) {
	var request = new XMLHttpRequest();
	request.onreadystatechange = function() { callback(request) }
	request.open(method, url);
	if (data == null) {
		request.send();
	} else {
		request.send(data);
	}
}

/**
 * Funzione utilizzata per rimandare alla pagina di errore personalizzata
 * @param codice : codice errore (status Http)
 * @param messaggio : messaggio di errore
 */
function sendError(codice, messaggio) {
	window.localStorage.setItem("errorCode", codice);
	window.localStorage.setItem("errorMsg", messaggio);
	window.location.href = "../error.html";
}

const voti = [
	"<vuoto>",
	"assente",
	"rimandato",
	"riprovato",
	"18",
	"19",
	"20",
	"21",
	"22",
	"23",
	"24",
	"25",
	"26",
	"27",
	"28",
	"29",
	"30",
	"30 e lode"
]

/**
 * Funzione comparatore tra esiti esame: restituisce un comparatore in ordine scelto e secondo il campo scelto
 * @param indice : indice del campo scelto
 * @param asc : indica se l'ordinamento è crescente, true indica crescente
 */
function comparatore(indice, asc) {
	return (r1, r2) => {
		if (!asc) {
			let tmp = r2;
			r2 = r1;
			r1 = tmp;
		}
		let val1, val2;
		val1 = r1.children[indice].textContent;
		val2 = r2.children[indice].textContent;
		if (indice == 5) {
			// sono due voti
			return voti.indexOf(val1.toString()) - voti.indexOf(val2.toString());
		}
		if (isNaN(val1) || isNaN(val2)) {
			// sono due stringhe
			return val1.toString().localeCompare(val2);
		}
		return val1 - val2;
	}
}






















