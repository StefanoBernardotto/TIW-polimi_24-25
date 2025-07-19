/**
 * Script per la home page dell docente
 */
{
	// Componenti della pagina
	let labelNome, anchorHome, anchorLogout, anchorVerbali, buttonIndietro, listaCorsi, listaAppelli, listaIscritti, modificaEsito, listaVerbali, verbale;

	// Oggetto che contiene lo studente loggato
	let docente;

	// verifica se l'utente è loggato, se non lo è rimanda alla pagina di login
	if (sessionStorage.getItem("docente") == null) {
		window.location.href = "login_docente.html";
	} else {
		docente = JSON.parse(sessionStorage.getItem("docente"));
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
			labelNome = new NomeDocente(
				docente["nome"] + " " + docente["cognome"],
				document.getElementById("label-nome")
			);

			anchorLogout = new AnchorLogout(
				document.getElementById("button-logout")
			);

			anchorHome = new AnchorHome(
				document.getElementById("anchor-home")
			);

			anchorVerbali = new AnchorVerbali(
				document.getElementById("anchor-verbali")
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

			listaIscritti = new ListaIscritti(
				document.getElementById("container-iscritti"),
				document.getElementById("header-iscritti"),
				document.getElementById("table-iscritti"),
				document.getElementById("table-body-iscritti"),
				[
					document.getElementById("th-matricola"),
					document.getElementById("th-cognome"),
					document.getElementById("th-nome"),
					document.getElementById("th-email"),
					document.getElementById("th-corso-laurea"),
					document.getElementById("th-voto"),
					document.getElementById("th-pubblicazione")
				],
				document.getElementById("button-box"),
				{
					pubblica: document.getElementById("button-pubblica"),
					verbalizza: document.getElementById("button-verbalizza"),
					inserimento: document.getElementById("button-inserimento-multiplo")
				}
			)

			modificaEsito = new ModificaEsito(
				document.getElementById("wrapper-modifica"),
				document.getElementById("container-modifica"),
				{
					corso: document.getElementById("label-corso"),
					data: document.getElementById("label-data"),
					matricola: document.getElementById("label-matricola"),
					nomeCognomeStudente: document.getElementById("label-nome-cognome-stud"),
					email: document.getElementById("label-email"),
					corsoLaurea: document.getElementById("label-corso-laurea")
				}
			);
		}

		// funzione che esegue il refresh della home page
		this.refreshPage = function() {
			listaAppelli.reset();
			modificaEsito.reset();
			listaCorsi.reset();
			listaIscritti.reset();
			labelNome.show();
			buttonIndietro.init();
			listaCorsi.show();
		};
	}

	// Label che mostra il nome del docente nella barra superiore
	function NomeDocente(_nomeCognome, labelNomeTopBar) {
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
	// questa funzione fa sì che venga ricaricato e mostrato il contenuto della pagina iscritti 
	// visualizzata in precedenza, funziona solo se il componente non viene resettato
	function goToIscrittiView() {
		listaIscritti.refresh();
	}
	function goToVerbaliView() {
		buttonIndietro.evolve();
		console.error("NOT IMPLEMENTED YET");
	}

	function ButtonIndietro(spanIndietro) {
		// imposta la destinazione "back" come la pagina di login
		this.init = function() {
			spanIndietro.removeEventListener("click", goToHomeView);
			spanIndietro.removeEventListener("click", goToVerbaliView);
			spanIndietro.removeEventListener("click", goToIscrittiView);
			spanIndietro.addEventListener("click", goToLoginPage);
		}
		// imposta la destinazione "back" come la pagina home
		this.evolve = function() {
			spanIndietro.removeEventListener("click", goToVerbaliView);
			spanIndietro.removeEventListener("click", goToLoginPage);
			spanIndietro.removeEventListener("click", goToIscrittiView);
			spanIndietro.addEventListener("click", goToHomeView);
		}
		// imposta la destinazione "back" come la pagina iscritti precedente
		this.evolveEsito = function() {
			spanIndietro.removeEventListener("click", goToHomeView);
			spanIndietro.removeEventListener("click", goToVerbaliView);
			spanIndietro.removeEventListener("click", goToLoginPage);
			spanIndietro.addEventListener("click", goToIscrittiView);
		}
		// imposta la destinazione "back" come la pagina verbali
		this.evolveVerbale = function() {
			spanIndietro.removeEventListener("click", goToLoginPage);
			spanIndietro.removeEventListener("click", goToHomeView);
			spanIndietro.removeEventListener("click", goToIscrittiView);
			spanIndietro.addEventListener("click", goToVerbaliView);
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
					window.sessionStorage.removeItem("docente");
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

	// Anchor per mostrare i verbali
	function AnchorVerbali(anchorVerbali) {
		anchorVerbali.addEventListener("click", goToVerbaliView, false);
	}

	// Tabella che mostra l'elenco dei corsi tenuti dal docente
	function ListaCorsi(_container, _table, _tableBody, _labelNoCorsi) {
		this.container = _container;
		this.table = _table;
		this.tableBody = _tableBody;
		this.labelNoCorsi = _labelNoCorsi;

		// funzione che interroga il server per ottenere la lista dei corsi e mostra la tabella con i corsi
		this.show = function() {
			this.container.style.display = "block";
			makeCall("GET", "../Corsi?profilo=docente", null, request => {
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
						window.location.href = "login_docente.html";
						window.sessionStorage.removeItem("docente");
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
			makeCall("GET", "../Appelli?profilo=docente&nome_corso=" + nomeCorso, null, request => {
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
						window.location.href = "login_docente.html";
						window.sessionStorage.removeItem("docente");
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
					listaIscritti.show(
						evt.target.closest("a").getAttribute("nome_corso"),
						evt.target.closest("a").getAttribute("data_appello")
					)
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

	// Tabella che mostra gli iscritti all'appello selezionato
	function ListaIscritti(_container, _header, _table, _tableBody, _tableHeaders, _buttonBox, _buttons) {
		this.container = _container;
		this.header = _header;
		this.table = _table;
		this.tableBody = _tableBody;
		this.tableHeaders = _tableHeaders;
		this.buttonBox = _buttonBox;
		this.buttons = _buttons;
		this.nomeCorso = null;
		this.dataAppello = null;
		this.asc = true;
		this.indicePrec = 0;
		let self = this;

		// istruzioni eseguite appena viene creato l'oggetto, inseriscono gli eventlistener sulle header per permettere di ordinare la tabella

		// per ogni header della tabella, inserisco l'evtListener per il click
		self.tableHeaders.forEach(th => {
			th.addEventListener("click", () => {
				// ottengo l'array con le righe della tabella
				const rows = Array.from(self.table.querySelectorAll("tbody > tr"));
				// ottengo l'indice della header nell'array di headers
				let indice = self.tableHeaders.indexOf(th);
				// aggiorno i valori di impostazione: se il campo è lo stesso del precedente ordinamento, inverto l'ordinamento
				// altrimenti setto l'ordinamento crescente e imposto l'ultimo campo scelto
				if (self.indicePrec == indice) {
					self.asc = !self.asc;
				} else {
					self.asc = true;
					self.indicePrec = indice;
				}
				// ordino la tabella con il comparatore in utils.js
				rows.sort(comparatore(indice, self.asc));
				// aggiungo nel body le righe ordinate
				rows.forEach(tr => {
					self.tableBody.appendChild(tr);
				})
			});
		})


		// funzione che ottiene la lista degli iscritti e la mostra a schermo
		this.show = function(nomeCorso, dataAppello) {
			buttonIndietro.evolve();
			this.tableBody.innerHTML = "";
			this.nomeCorso = nomeCorso;
			this.dataAppello = dataAppello;
			makeCall("GET", "../Iscritti?nome_corso=" + nomeCorso + "&data_appello=" + dataAppello, null, request => {
				if (request.readyState == XMLHttpRequest.DONE) {
					if (request.status == 200) {
						let iscritti = JSON.parse(request.responseText);
						if (iscritti.length == 0) {
							this.header.textContent = "Nessun iscritto a '" + nomeCorso + "', appello del " + dataAppello;
							this.table.style.display = "none";
							this.buttonBox.style.display = "none";
						} else {
							this.header.textContent = "Iscritti a '" + nomeCorso + "', appello del " + dataAppello;
							this.table.style.display = "block";
							this.buttonBox.style.display = "flex";
							if (iscritti.some(iscrizione => iscrizione.first.statoPubblicazione == "inserito")) {
								this.buttons.pubblica.style.display = "block";
							} else {
								this.buttons.pubblica.style.display = "none";
							}
							if (iscritti.some(iscrizione => (iscrizione.first.statoPubblicazione == "pubblicato" || iscrizione.statoPubblicazione == "rifiutato"))) {
								this.buttons.verbalizza.style.display = "block";
							} else {
								this.buttons.verbalizza.style.display = "none";
							}
							this.updateData(iscritti);
						}
						this.container.style.display = "block";
					} else if (request.status == 401) {
						window.location.href = "login_docente.html";
						sessionStorage.removeItem("docente");
					} else {
						sendError(request.status, request.responseText)
					}
				}
			});
		}

		// funzione che crea e carica la tabella vera e propria
		this.updateData = function(iscritti) {
			console.log(iscritti);
			iscritti.forEach(iscrizione => {
				let row = document.createElement("tr");
				let matricolaCell = document.createElement("td");
				matricolaCell.textContent = iscrizione.first.matricolaStudente;
				row.appendChild(matricolaCell);
				let cognomeCell = document.createElement("td");
				cognomeCell.textContent = iscrizione.second.cognome;
				row.appendChild(cognomeCell);
				let nomeCell = document.createElement("td");
				nomeCell.textContent = iscrizione.second.nome;
				row.appendChild(nomeCell);
				let emailCell = document.createElement("td");
				emailCell.textContent = iscrizione.second.email;
				row.appendChild(emailCell);
				let corsoLaureaCell = document.createElement("td");
				corsoLaureaCell.textContent = iscrizione.second.corsoLaurea;
				row.appendChild(corsoLaureaCell);
				let votoCell = document.createElement("td");
				votoCell.textContent = iscrizione.first.voto;
				row.appendChild(votoCell);
				let pubblicazioneCell = document.createElement("td");
				pubblicazioneCell.textContent = iscrizione.first.statoPubblicazione;
				row.appendChild(pubblicazioneCell);
				let linkCell = document.createElement("td");
				let linkImage = document.createElement("img");
				linkImage.src = "../imgs/edit.png"
				linkImage.className = "edit-icon";
				linkCell.appendChild(linkImage);
				row.appendChild(linkCell);
				self.tableBody.appendChild(row);
			})
		}

		// funzione che resetta il componente: svuota la tabella e resetta i due campi data e corso
		this.reset = function() {
			this.tableBody.innerHTML = "";
			this.nomeCorso = null;
			this.dataAppello = null;
			this.hide();
		}

		// funzione che nasconde temporaneamente il componente
		this.hide = function() {
			this.container.style.display = "none";
		}
	}

	// Form che mostra l'esito dell'esame selezionato
	function ModificaEsito(_wrapper, _container, _labelNoEsito, _labels) {
		this.wrapper = _wrapper;
		this.container = _container;
		this.labelNoEsito = _labelNoEsito;
		this.labels = _labels;
		let self = this;

		// funzione che ottiene l'esito dal server e lo mostra a schermo
		this.show = function(nomeCorso, dataAppello, matricola) {
			this.nomeCorso = nomeCorso;
			this.dataAppello = dataAppello;
			this.wrapper.style.display = "block";
			buttonIndietro.evolve();
			makeCall("GET",
				"../EsitoEsame?profilo=docente" + "&nome_corso=" + nomeCorso + "&data_appello=" + dataAppello,
				null, request => {
					if (request.readyState == XMLHttpRequest.DONE) {
						if (request.status == 200) { // OK: richiesta valida






						} else if (request.status == 401) { // UNAUTHORIZED: utente non verificato
							window.location.href = "login_docente.html";
							window.sessionStorage.removeItem("docente");
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
		}

		// funzione che nasconde il form e inizializza gli attributi dell'appello salvato
		this.reset = function() {
			this.wrapper.style.display = "none";
			this.container.style.display = "none";
			this.nomeCorso = null;
			this.dataAppello = null;
		}
	}










}