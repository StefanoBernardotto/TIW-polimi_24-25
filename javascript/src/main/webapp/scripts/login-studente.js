/**
 * script per la gestione del login di un docente
 */
{
	let loginForm = document.getElementById("login-form");
	let loginErrorMessage = document.getElementById("login-error-message");
	loginForm.addEventListener("submit", (evt) => {
		evt.preventDefault();
		if (loginForm.checkValidity()) {
			let codice = parseInt(loginForm.elements["codice"].value);
			let profilo = loginForm.elements["profilo"].value;
			if (isNaN(codice) || codice < 100000 || codice > 999999) {
				loginErrorMessage.textContent = "Formato codice non valido";
			} else if (profilo !== "studente") {
				loginErrorMessage.textContent = "Richiesta errata";
			} else {
				let data = new FormData(loginForm);
				makeCall("POST", "../VerificaLogin", data, (response) => {
					if (response.readyState == XMLHttpRequest.DONE) {
						switch (response.status) {
							case 200: { // OK: credenziali valide
								sessionStorage.setItem("studente", response.responseText);
								window.location.href = "home_studente.html";
								break;
							}
							// case a cascata, lo utilizzo per avere case multipli (non permessi in js con ",")
							case 400: // BAD_REQUEST: formato codice errato
							case 401: // UNAUTHORIZED: credenziali non valide
							case 404: // NOT_FOUND: server non trovato
							case 500: //INTERNAL_SERVER_ERROR: errore nel server
								{
									loginErrorMessage.textContent = response.responseText;
									break;
								}
						}
					}
				});
			}
		} else {
			loginForm.reportValidity();
		}
	});
}