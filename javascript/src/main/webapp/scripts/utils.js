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
function sendError(codice, messaggio){
	window.localStorage.setItem("errorCode", codice);
	window.localStorage.setItem("errorMsg", messaggio);
	window.location.href = "../error.html";
}