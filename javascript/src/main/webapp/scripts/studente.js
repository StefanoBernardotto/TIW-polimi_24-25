/**
 * Script per la home page dello studente
 */
{
	// Componenti della pagina
	let labelNome, anchorHome, anchorLogout, buttonIndietro, listaCorsi, listaAppelli, esitoEsame, messaggioPopup;

	// Oggetto che contiene lo studente loggato
	let studente;

	// verifica se l'utente è loggato, se non lo è rimanda alla pagina di login
	if (sessionStorage.getItem("studente") == null) {
		window.location.href = "login_studente.html";
	} else {
		studente = JSON.parse(sessionStorage.getItem("studente"));
		var pageOrchestrator = new PageOrchestrator();
		pageOrchestrator.start();
		pageOrchestrator.refreshPage();
	}

	/**
	 * Costruttore del controller della pagina.
	 */
	function PageOrchestrator() {
		// funzione che crea ed inizializza i componenti della pagina
		this.start = function() {
			labelNome = new NomeStudente(
				studente["nome"] + " " + studente["cognome"],
				document.getElementById("label-nome")
			);

			anchorLogout = new AnchorLogout(
				document.getElementById("button-logout")
			);

			anchorHome = new AnchorHome(
				document.getElementById("anchor-home")
			);

			buttonIndietro = new ButtonIndietro(
				document.getElementById("back-anchor")
			);

			listaCorsi = new ListaCorsi(
				document.getElementById("container-corsi"),
				document.getElementById("table-corsi"),
				document.getElementById("table-body-corsi"),
				document.getElementById("no-content-message")
			);

			listaAppelli = new ListaAppelli(
				document.getElementById("wrapper-appelli"),
				document.getElementById("container-appelli"),
				document.getElementById("table-appelli"),
				document.getElementById("table-body-appelli"),
				document.getElementById("label-no-appelli")
			);

			esitoEsame = new EsitoEsame(
				document.getElementById("wrapper-esito"),
				document.getElementById("container-esito"),
				document.getElementById("header-no-esito"),
				{
					corso: document.getElementById("label-corso"),
					data: document.getElementById("label-data"),
					matricola: document.getElementById("label-matricola"),
					nomeCognomeStudente: document.getElementById("label-nome-cognome-stud"),
					email: document.getElementById("label-email"),
					corsoLaurea: document.getElementById("label-corso-laurea"),
					voto: document.getElementById("label-voto"),
					pubblicazione: document.getElementById("label-pubblicazione")
				},
				document.getElementById("icon-cestino"),
				document.getElementById("label-non-rifiutabile")
			);

			messaggioPopup = new MessaggioPopup(
				document.getElementById("alert-layer"),
				document.getElementById("alert-messaggio"),
				document.getElementById("alert-button-cancella"),
				document.getElementById("alert-button-conferma")
			);
		}

		// funzione che esegue il refresh della home page
		this.refreshPage = function() {
			listaAppelli.reset();
			esitoEsame.reset();
			listaCorsi.reset();
			labelNome.show();
			buttonIndietro.init();
			listaCorsi.show();
		};
	}

	// Label che mostra il nome dello studente nella barra superiore
	function NomeStudente(_nomeCognome, labelNomeTopBar) {
		this.nomeCognome = _nomeCognome;
		this.show = function() {
			labelNomeTopBar.textContent = this.nomeCognome;
		};
	}

	// Bottone per tornare alla vista precedente e relative funzioni di callback
	function goToHomeView() {
		pageOrchestrator.refreshPage();
	}
	function goToLoginPage() {
		window.location.href = "../index.html";
	}

	function ButtonIndietro(spanIndietro) {
		this.init = function() {
			spanIndietro.removeEventListener("click", goToHomeView);
			spanIndietro.addEventListener("click", goToLoginPage);
		}
		this.evolve = function() {
			spanIndietro.removeEventListener("click", goToLoginPage);
			spanIndietro.addEventListener("click", goToHomeView);
		}
	}

	// Anchor per il logout e relativa funzione di logout
	function AnchorLogout(anchorLogout) {
		anchorLogout.addEventListener("click", logout, false);
	}

	function logout() {
		makeCall("GET", "../Logout", null, request => {
			if (request.readyState == XMLHttpRequest.DONE) {
				if (request.status == 200) {
					window.sessionStorage.removeItem("studente");
					window.location.href = "../index.html";
				} else {
					sendError(request.status, request.responseText);
				}
			}
		})
	}

	// Anchor per tornare alla home
	function AnchorHome(anchorHome) {
		anchorHome.addEventListener("click", goToHomeView, false);
	}

	// Tabella che mostra l'elenco dei corsi a cui è iscritto lo studente
	function ListaCorsi(_container, _table, _tableBody, _labelNoCorsi) {
		this.container = _container;
		this.table = _table;
		this.tableBody = _tableBody;
		this.labelNoCorsi = _labelNoCorsi;

		// funzione che interroga il server per ottenere la lista dei corsi e mostra la tabella con i corsi
		this.show = function() {
			this.container.style.display = "block";
			makeCall("GET", "../Corsi?profilo=studente", null, request => {
				if (request.readyState == XMLHttpRequest.DONE) {
					if (request.status == 200) { // OK: richiesta valida
						let listaCorsi = JSON.parse(request.responseText);
						if (listaCorsi.length == 0) {
							this.container.visibility = "hidden";
							this.labelNoCorsi.textContent = "Nessun corso disponibile";
							this.labelNoCorsi.style.visibility = "visible";
						} else {
							this.updateData(listaCorsi);
						}
					} else if (request.status == 401) { // UNAUTHORIZED: utente non verificato
						window.location.href = "login_studente.html";
						window.sessionStorage.removeItem("studente");
					} else { // Errore nella richiesta o nel server
						sendError(request.status, request.responseText);
					}
				}
			})
		};

		// funzione che popola la tabella vera e propria
		this.updateData = function(listaCorsi) {
			listaCorsi.forEach(corso => {
				let row = document.createElement("tr");
				let nomeCell = document.createElement("td");
				nomeCell.textContent = corso.nome;
				row.appendChild(nomeCell);
				let docenteCell = document.createElement("td");
				docenteCell.textContent = corso.nomeCognomeDocente;
				row.appendChild(docenteCell);
				let linkCell = document.createElement("td");
				let anchor = document.createElement("a");
				anchor.setAttribute("nome_corso", corso.nome);
				anchor.addEventListener("click", evt => {
					document.querySelectorAll("#table-corsi tr").forEach(row => row.className = "");
					evt.target.closest("tr").className = "selected";
					listaAppelli.reset();
					listaAppelli.show(evt.target.closest("a").getAttribute("nome_corso"));
				}, false);
				anchor.href = "#";
				let image = document.createElement("img");
				image.src = "../imgs/goto.png";
				image.className = "goto-icon";
				anchor.appendChild(image);
				linkCell.appendChild(anchor);
				row.appendChild(linkCell);
				this.tableBody.appendChild(row);
			});
			this.table.style.visibility = "visible";
			this.labelNoCorsi.style.visibility = "hidden";
		};

		// funzione che svuota la tabelle e la nasconde
		this.reset = function() {
			this.tableBody.innerHTML = "";
			this.container.style.display = "none";
		}
	}

	// Tabella che mostra l'elenco degli apppelli relativi al corso scelto
	function ListaAppelli(_wrapper, _container, _table, _tableBody, _labelNoAppelli) {
		this.wrapper = _wrapper;
		this.container = _container;
		this.table = _table;
		this.tableBody = _tableBody;
		this.labelNoAppelli = _labelNoAppelli;
		this.nomeCorso = null;

		// funzione che ottiene gli appelli dal server e mostra la tabella
		this.show = function(nomeCorso) {
			this.nomeCorso = nomeCorso;
			this.wrapper.style.display = "block";
			makeCall("GET", "../Appelli?profilo=studente&nome_corso=" + nomeCorso, null, request => {
				if (request.readyState == XMLHttpRequest.DONE) {
					if (request.status == 200) { // OK: richiesta valida
						let listaAppelli = JSON.parse(request.responseText);
						if (listaAppelli.length == 0) {
							this.labelNoAppelli.textContent = "Nessun appello per il corso";
							this.labelNoAppelli.style.visibility = "visible";
							this.container.style.visibility = "hidden";
						} else {
							this.updateData(listaAppelli);
						}
					} else if (request.status == 401) { // UNAUTHORIZED: utente non verificato
						window.location.href = "login_studente.html";
						window.sessionStorage.removeItem("studente");
					} else { // Errore nel server o nella richiesta
						sendError(request.status, request.responseText);
					}
				}
			});
		};

		// costruisce la tabella vera e propria con la lista passata
		this.updateData = function(listaAppelli) {
			this.container.style.visibility = "visible";
			this.labelNoAppelli.style.visibility = "hidden";
			let self = this;
			listaAppelli.forEach(appello => {
				let row = document.createElement("tr");
				let dataCell = document.createElement("td");
				dataCell.textContent = appello.data;
				row.appendChild(dataCell);
				let linkCell = document.createElement("td");
				let anchor = document.createElement("a");
				anchor.setAttribute("data_appello", appello.data);
				anchor.setAttribute("nome_corso", self.nomeCorso);
				anchor.addEventListener("click", evt => {
					self.reset();
					listaCorsi.reset();
					esitoEsame.show(
						evt.target.closest("a").getAttribute("nome_corso"),
						evt.target.closest("a").getAttribute("data_appello")
					);
				}, false);
				anchor.href = "#";
				let image = document.createElement("img");
				image.src = "../imgs/goto.png";
				image.className = "goto-icon";
				anchor.appendChild(image);
				linkCell.appendChild(anchor);
				row.appendChild(linkCell);
				self.tableBody.appendChild(row);
			})
		}

		// svuota la tabella e nasconde il componente
		this.reset = function() {
			this.nomeCorso = null;
			this.tableBody.innerHTML = "";
			this.wrapper.style.display = "none";
		}
	}

	// Form che mostra l'esito dell'esame selezionato
	function EsitoEsame(_wrapper, _container, _labelNoEsito, _labels, _cestino, _labelNonRifiutabile) {
		this.wrapper = _wrapper;
		this.container = _container;
		this.labelNoEsito = _labelNoEsito;
		this.labels = _labels;
		this.cestino = _cestino;
		this.labelNonRifiutabile = _labelNonRifiutabile;
		let self = this;

		// funzioni per abilitare il drag and drop per il rifiuto del voto
		this.cestino.addEventListener("dragover", evt => {
			evt.preventDefault();
			evt.target.classList.add("dragging-over");
		});

		this.cestino.addEventListener("dragleave", () => {
			self.cestino.classList.remove("dragging-over");
		});

		this.cestino.addEventListener("drop", evt => {
			self.cestino.classList.remove("dragging-over");
			messaggioPopup.show(
				"Vuoi davvero rifiutare il voto?",
				self.rifiutaEsito,
				null
			)
		})

		// funzione che ottiene l'esito dal server e lo mostra a schermo
		this.show = function(nomeCorso, dataAppello) {
			this.nomeCorso = nomeCorso;
			this.dataAppello = dataAppello;
			this.wrapper.style.display = "block";
			buttonIndietro.evolve();
			makeCall("GET",
				"../EsitoEsame?profilo=studente" + "&nome_corso=" + nomeCorso + "&data_appello=" + dataAppello,
				null, request => {
					if (request.readyState == XMLHttpRequest.DONE) {
						if (request.status == 200) { // OK: richiesta valida
							let iscrizione = JSON.parse(request.responseText);
							if (iscrizione == null) {
								this.labelNoEsito.textContent = "Non iscritto all'appello";
								this.labelNoEsito.style.display = "block";
								this.container.style.display = "none";
								this.cestino.style.display = "none";
							} else if (iscrizione.statoPubblicazione != "pubblicato"
								&& iscrizione.statoPubblicazione != "verbalizzato"
								&& iscrizione.statoPubblicazione != "rifiutato") {
								this.labelNoEsito.textContent = "Esito non ancora pubblicato";
								this.labelNoEsito.style.display = "block";
								this.container.style.display = "none";
								this.cestino.style.display = "none";
							} else {
								this.updateForm(iscrizione);
							}
						} else if (request.status == 401) { // UNAUTHORIZED: utente non verificato
							window.location.href = "login_studente.html";
							window.sessionStorage.removeItem("studente");
						} else { // Errore nel server o nella richiesta
							sendError(request.status, request.responseText);
						}
					}
				});
		}

		// funzione per riempire e mostrare il form vero e proprio
		this.updateForm = function(iscrizione) {
			this.container.style.display = "block";
			this.labelNoEsito.style.display = "none";
			this.labels.corso.textContent = iscrizione.nomeCorso;
			this.labels.data.textContent = iscrizione.dataAppello;
			this.labels.matricola.textContent = studente.matricola;
			this.labels.nomeCognomeStudente.textContent = studente.nome + " " + studente.cognome;
			this.labels.email.textContent = studente.email;
			this.labels.corsoLaurea.textContent = studente.corsoLaurea;
			this.labels.voto.textContent = iscrizione.voto;
			this.labels.pubblicazione.textContent = iscrizione.statoPubblicazione;
			if (iscrizione.statoPubblicazione == "pubblicato") {
				// rifiutabile
				if (voti.indexOf(iscrizione.voto) > 3) {
					this.cestino.style.display = "block";
					this.labelNonRifiutabile.style.display = "none";
				} else {
					this.cestino.style.display = "none";
					this.labelNonRifiutabile.style.display = "none"
				}
			} else if (iscrizione.statoPubblicazione == "rifiutato") {
				// già rifiutato
				this.cestino.style.display = "none";
				this.labelNonRifiutabile.textContent = "Il voto è stato rifiutato";
				this.labelNonRifiutabile.style.display = "block";
			} else if (iscrizione.statoPubblicazione = "verbalizzato") {
				// verbalizzato
				this.cestino.style.display = "none";
				this.labelNonRifiutabile.textContent = "Il voto è stato verbalizzato";
				this.labelNonRifiutabile.style.display = "block";
			}
		}

		// funzione che nasconde il form e inizializza gli attributi dell'appello salvato
		this.reset = function() {
			this.wrapper.style.display = "none";
			this.container.style.display = "none";
			this.cestino.style.display = "none";
			this.nomeCorso = null;
			this.dataAppello = null;
		}

		// funzione per il rifiuto dell'esito: invia la richiesta al server
		this.rifiutaEsito = function() {
			data = new FormData();
			data.append("profilo", "studente");
			data.append("azione", "rifiuta");
			data.append("nome_corso", self.nomeCorso);
			data.append("data_appello", self.dataAppello);
			makeCall("POST", "../EsitoEsame", data, request => {
				if (request.readyState == XMLHttpRequest.DONE) {
					if (request.status == 200) { // OK: richiesta valida
						let dataAppello = self.dataAppello;
						let nomeCorso = self.nomeCorso;
						self.reset();
						self.show(nomeCorso, dataAppello);
					} else if (request.status == 401) { // UNAUTHORIZED: utente non verificato
						window.location.href = "login_studente.html";
						window.sessionStorage.removeItem("studente");
					} else { // Errore nella richiesta o nel server
						sendError(request.status, request.responseText);
					}
				}
			});
		}
	}

	// Alert per il rifiuto del voto
	function MessaggioPopup(_container, _labelMessaggio, _buttonCancella, _buttonConferma) {
		this.container = _container;
		this.labelMessaggio = _labelMessaggio;
		this.buttonConferma = _buttonConferma;
		this.buttonCancella = _buttonCancella;
		let self = this;

		/**
		 * funzione che mostra il popup di conferma con il messaggio selezionato
		 * @param messaggio : messaggio da mostrare
		 * @param onConferma : funzione eseguita in caso di conferma dell'utente
		 * @param onCancella : funzione eseguita in caso di annullamento dell'utente 
		 */
		this.show = function(messaggio, onConferma, onCancella) {
			self.container.style.display = "flex";
			self.labelMessaggio.textContent = messaggio;
			self.buttonConferma.addEventListener("click", () => {
				self.close();
				if (onConferma != null) {
					onConferma();
				}
			}, false);
			self.buttonCancella.addEventListener("click", () => {
				self.close();
				if (onCancella != null) {
					onCancella();
				}
			}, false);
		}

		// funzione che nasconde il popup
		this.close = function() {
			this.container.style.display = "none";
		}
	}










}