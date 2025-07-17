/**
 * funzioni di utilità
 */

/**
 * Funzione utilizzata per effettuare le chiamate asincrone al server
 * @param method : the method of the Http request
 * @param url : the url of the Http request
 * @param data : the FormData to send in the Http request
 * @param callback : the function to execute when the response is ready
 */
function makeCall(method, url, data, callback) {
	var request = new XMLHttpRequest();
	request.onreadystatechange = function() { callback(request) }
	request.open(method, url);
	if(data == null){
		request.send();
	}else{
		request.send(data);
	}
}