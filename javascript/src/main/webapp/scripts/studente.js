/**
 * Script per la home page dello studente
 */
{
	// Componenti della pagina
	let labelNome, buttonIndietro, listaCorsi, listaAppelli, esitoEsame;

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
			
			buttonIndietro = new ButtonIndietro(
				document.getElementById("back-anchor")
			)

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
				document.getElementById("button-rifiuta"),
				document.getElementById("label-non-rifiutabile")
			)
		}
		// funzione che simula il refresh della home page
		this.refreshPage = function() {
			labelNome.show();
			buttonIndietro.init();
			listaCorsi.reset();
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
	
	function tmpFuncHome(){
		window.location.href = "home_studente.html";
	}
	function tmpFuncLogin(){
		window.location.href = "../index.html";
	}
	
	function ButtonIndietro(spanIndietro){
		this.init = function(){
			spanIndietro.removeEventListener("click", tmpFuncHome);
			spanIndietro.addEventListener("click", tmpFuncLogin);
		}
		this.evolve = function(){
			spanIndietro.removeEventListener("click", tmpFuncLogin);
			spanIndietro.addEventListener("click", tmpFuncHome);
		}
	}

	// Tabella che mostra l'elenco dei corsi a cui è iscritto lo studente
	function ListaCorsi(_container, _table, _tableBody, _labelNoCorsi) {
		this.container = _container;
		this.table = _table;
		this.tableBody = _tableBody;
		this.labelNoCorsi = _labelNoCorsi;
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
					} else if (request.status == 401) {
						window.location.href = "login_studente.html";
						window.sessionStorage.removeItem("studente");
					} else {
						this.container.visibility = "hidden";
						this.labelNoCorsi.textContent = request.responseText;
						this.labelNoCorsi.style.visibility = "visible";
					}
				}
			})
		};
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
					if (request.status == 200) {
						let listaAppelli = JSON.parse(request.responseText);
						if (listaAppelli.length == 0) {
							this.labelNoAppelli.textContent = "Nessun appello per il corso";
							this.labelNoAppelli.style.visibility = "visible";
							this.container.style.visibility = "hidden";
						} else {
							this.updateData(listaAppelli);
						}
					} else if (request.status == 401) {
						window.location.href = "login_studente.html";
						window.sessionStorage.removeItem("studente");
					} else {
						this.labelNoAppelli.textContent = request.responseText;
						this.labelNoAppelli.style.visibility = "visible";
						this.container.style.visibility = "hidden";
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
	function EsitoEsame(_wrapper, _container, _labelNoEsito, _labels, _buttonRifiuta, _labelNonRifiutabile) {
		this.wrapper = _wrapper;
		this.container = _container;
		this.labelNoEsito = _labelNoEsito;
		this.labels = _labels;
		this.buttonRifiuta = _buttonRifiuta;
		this.labelNonRifiutabile = _labelNonRifiutabile;
		// funzione che ottiene l'esito dal server e lo mostra a schermo
		this.show = function(nomeCorso, dataAppello) {
			this.wrapper.style.display = "block";
			buttonIndietro.evolve();
			makeCall("GET",
				"../EsitoEsame?profilo=studente" + "&nome_corso=" + nomeCorso + "&data_appello=" + dataAppello,
				null, request => {
					if (request.readyState == XMLHttpRequest.DONE) {
						if (request.status == 200) {
							let iscrizione = JSON.parse(request.responseText);
							if (iscrizione == null) {
								this.labelNoEsito.textContent = "Non iscritto all'appello";
								this.labelNoEsito.style.display = "block";
								this.container.style.display = "none";
							} else if (iscrizione.statoPubblicazione != "pubblicato"
								&& iscrizione.statoPubblicazione != "verbalizzato"
								&& iscrizione.statoPubblicazione != "rifiutato") {
								this.labelNoEsito.textContent = "Esito non ancora pubblicato";
								this.labelNoEsito.style.display = "block";
								this.container.style.display = "none";
							} else {
								this.updateForm(iscrizione);
							}
						} else if (request.status == 401) {
							window.location.href = "login_studente.html";
							window.sessionStorage.removeItem("studente");
						} else {
							this.labelNoEsito.textContent = request.responseText;
							this.labelNoEsito.style.display = "block";
							this.container.style.display = "none";
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
				this.buttonRifiuta.style.display = "block";
				this.labelNonRifiutabile.style.display = "none";
			} else if (iscrizione.statoPubblicazione == "rifiutato") {
				// già rifiutato
				this.buttonRifiuta.style.display = "none";
				this.labelNonRifiutabile.textContent = "Il voto è stato rifiutato";
				this.labelNonRifiutabile.style.display = "block";
			} else if (iscrizione.statoPubblicazione = "verbalizzato") {
				// verbalizzato
				this.buttonRifiuta.style.display = "none";
				this.labelNonRifiutabile.textContent = "Il voto è stato verbalizzato";
				this.labelNonRifiutabile.style.display = "block";
			}
		}
		this.reset = function() {
			this.wrapper.style.display = "none";
		}
	}





















}