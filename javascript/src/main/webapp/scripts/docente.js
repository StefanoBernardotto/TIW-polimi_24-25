/**
 * Script per la home page dell docente
 */
{
	// Componenti della pagina
	let labelNome, anchorHome, anchorLogout, anchorVerbali, buttonIndietro, listaCorsi, listaAppelli, listaIscritti, modificaEsito, finestraModale, listaVerbali, visualizzaVerbale;

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
				},
				document.getElementById("select-voto"),
				document.getElementById("button-salva")
			);

			finestraModale = new FinestraModale(
				document.getElementById("modal-layer"),
				document.getElementById("modal-table"),
				document.getElementById("modal-table-body"),
				document.getElementById("modal-btn-salva"),
				document.getElementById("modal-btn-annulla")
			)

			listaVerbali = new ListaVerbali(
				document.getElementById("wrapper-verbali"),
				document.getElementById("table-verbali"),
				document.getElementById("table-body-verbali")
			)

			visualizzaVerbale = new VisualizzaVerbale(
				document.getElementById("wrapper-verbale"),
				document.getElementById("table-verbale"),
				document.getElementById("table-body-verbale"),
				{
					codice: document.getElementById("v-label-codice"),
					dataCreazione: document.getElementById("v-label-data-creaz"),
					oraCreazione: document.getElementById("v-label-ora-creaz"),
					corso: document.getElementById("v-label-corso"),
					data: document.getElementById("v-label-data")
				}
			)
		}

		// funzione che esegue il refresh della home page
		this.refreshPage = function() {
			listaAppelli.reset();
			listaVerbali.reset();
			visualizzaVerbale.reset();
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
		visualizzaVerbale.reset();
		modificaEsito.reset();
		listaIscritti.refresh();
	}
	function goToVerbaliView() {
		buttonIndietro.evolve();
		listaCorsi.reset();
		listaAppelli.reset();
		listaIscritti.reset();
		modificaEsito.reset();
		visualizzaVerbale.reset();
		listaVerbali.reset();
		listaVerbali.show();
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
				const rows = Array.from(self.table.querySelectorAll("tbody > tr"));
				let indice = self.tableHeaders.indexOf(th);
				// aggiorno i valori di impostazione: se il campo è lo stesso del precedente ordinamento, inverto l'ordinamento
				// altrimenti setto l'ordinamento crescente e imposto l'ultimo campo scelto
				if (self.indicePrec == indice) {
					self.asc = !self.asc;
				} else {
					self.asc = true;
					self.indicePrec = indice;
				}

				rows.sort(comparatore(indice, self.asc));
				rows.forEach(tr => {
					self.tableBody.appendChild(tr);
				})
			});
		});

		this.buttons.pubblica.addEventListener("click", () => {
			self.pubblicaVoti();
			self.refresh();
		});

		this.buttons.verbalizza.addEventListener("click", () => {
			self.verbalizzaVoti();
		});

		this.buttons.inserimento.addEventListener("click", () => {
			finestraModale.show(self.nomeCorso, self.dataAppello, self.tableBody);
		});

		this.pubblicaVoti = function() {
			makeCall("POST", "../Iscritti?nome_corso=" + self.nomeCorso + "&data_appello=" + self.dataAppello + "&azione=pubblica", null, request => {
				if (request.readyState == XMLHttpRequest.DONE) {
					if (request.status == 200) {

					} else if (request.status == 401) {
						window.location.href = "login_docente.html";
						sessionStorage.removeItem("docente");
					} else {
						sendError(request.status, request.responseText)
					}
				}
			})
		}

		this.verbalizzaVoti = function() {
			makeCall("POST", "../Iscritti?nome_corso=" + self.nomeCorso + "&data_appello=" + self.dataAppello + "&azione=verbalizza", null, request => {
				if (request.readyState == XMLHttpRequest.DONE) {
					if (request.status == 200) {
						let codiceVerbale = request.responseText;
						self.hide();
						buttonIndietro.evolveEsito();
						visualizzaVerbale.show(codiceVerbale);
					} else if (request.status == 401) {
						window.location.href = "login_docente.html";
						sessionStorage.removeItem("docente");
					} else {
						sendError(request.status, request.responseText)
					}
				}
			})
		}

		// funzione che ottiene la lista degli iscritti e la mostra a schermo
		this.show = function(nomeCorso, dataAppello) {
			buttonIndietro.evolve();
			self.tableBody.innerHTML = "";
			self.nomeCorso = nomeCorso;
			self.dataAppello = dataAppello;
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
							if (iscritti.some(iscrizione => iscrizione.first.statoPubblicazione == "non inserito")) {
								this.buttons.inserimento.style.display = "block";
							} else {
								this.buttons.inserimento.style.display = "none";
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
				if (iscrizione.first.statoPubblicazione == "non inserito" || iscrizione.first.statoPubblicazione == "inserito") {
					let linkImage = document.createElement("img");
					linkImage.src = "../imgs/edit.png"
					linkImage.className = "edit-icon";
					linkCell.setAttribute("nome_corso", self.nomeCorso);
					linkCell.setAttribute("data_appello", self.dataAppello);
					linkCell.setAttribute("matricola", iscrizione.first.matricolaStudente);
					linkImage.addEventListener("click", (evt) => {
						listaIscritti.hide();
						modificaEsito.show(
							evt.target.closest("td").getAttribute("nome_corso"),
							evt.target.closest("td").getAttribute("data_appello"),
							evt.target.closest("td").getAttribute("matricola")
						);
					});
					linkCell.appendChild(linkImage);
				}
				row.appendChild(linkCell);
				self.tableBody.appendChild(row);
			})
		}

		// funzione che resetta il componente: svuota la tabella e resetta i due campi data e corso
		this.reset = function() {
			self.tableBody.innerHTML = "";
			self.nomeCorso = null;
			self.dataAppello = null;
			self.hide();
		}

		// funzione che nasconde temporaneamente il componente
		this.hide = function() {
			self.container.style.display = "none";
		}

		// funzione che aggiorna il contenuto con i dati del server utilizzando gli attributi nomeCorso 
		// e dataAppello già salvati, non funziona dopo reset!!!
		this.refresh = function() {
			let nomeTmp = self.nomeCorso, dataTmp = self.dataAppello;
			self.reset()
			self.show(nomeTmp, dataTmp);
		}
	}

	// Form che mostra l'esito dell'esame selezionato
	function ModificaEsito(_wrapper, _container, _labels, _select, _button) {
		this.wrapper = _wrapper;
		this.container = _container;
		this.labels = _labels;
		this.select = _select;
		this.button = _button;
		let self = this;

		// istruzioni che caricano i possibili voti nella select
		voti.forEach(voto => {
			if (voto != "<vuoto>") {
				let opt = document.createElement("option");
				opt.textContent = voto;
				this.select.appendChild(opt);
			}
		});

		// istruzione per aggiungere l'evt listener al click del bottone per salvare
		this.button.addEventListener("click", () => {
			// ottiene il valore dell'opzione selezionata
			let voto = Array.from(self.select.children).find(opt => opt.selected).textContent;
			self.modificaVoto(voto);
		});

		// funzione per comunicare al server la modifica del voto
		this.modificaVoto = function(voto) {
			let data = new FormData();
			data.append("nome_corso", self.nomeCorso);
			data.append("data_appello", self.dataAppello);
			data.append("matricola", self.matricola);
			data.append("voto", voto);
			data.append("profilo", "docente");
			data.append("azione", "modifica");
			makeCall("POST", "../EsitoEsame", data, request => {
				if (request.readyState == XMLHttpRequest.DONE) {
					if (request.status == 200) {
						self.reset();
						listaIscritti.refresh();
						buttonIndietro.evolve();
					} else if (request.status == 401) {
						window.location.href = "login_docente.html";
						sessionStorage.removeItem("docente");
					} else {
						sendError(request.status, request.responseText)
					}
				}
			})
		}

		// funzione che ottiene l'esito dal server e lo mostra a schermo
		this.show = function(nomeCorso, dataAppello, matricola) {
			this.nomeCorso = nomeCorso;
			this.dataAppello = dataAppello;
			this.matricola = matricola;
			buttonIndietro.evolveEsito();
			makeCall("GET",
				"../EsitoEsame?profilo=docente" + "&nome_corso=" + nomeCorso + "&data_appello=" + dataAppello + "&matricola=" + matricola,
				null, request => {
					if (request.readyState == XMLHttpRequest.DONE) {
						if (request.status == 200) { // OK: richiesta valida
							let iscrizione = JSON.parse(request.responseText);
							self.updateForm(iscrizione)
							this.wrapper.style.display = "block";
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
			this.labels.corso.textContent = iscrizione.first.nomeCorso;
			this.labels.data.textContent = iscrizione.first.dataAppello;
			this.labels.matricola.textContent = iscrizione.second.matricola;
			this.labels.nomeCognomeStudente.textContent = iscrizione.second.nome + " " + iscrizione.second.cognome;
			this.labels.email.textContent = iscrizione.second.email;
			this.labels.corsoLaurea.textContent = iscrizione.second.corsoLaurea;
			// carica il valore precedente del voto dalla lista di opzioni
			Array.from(this.select.children).forEach(option => option.selected = option.textContent == iscrizione.first.voto);
		}

		// funzione che nasconde il form e inizializza gli attributi dell'appello salvato
		this.reset = function() {
			this.wrapper.style.display = "none";
			this.container.style.display = "none";
			this.nomeCorso = null;
			this.dataAppello = null;
		}
	}

	// Finestra modale per l'inserimento multiplo
	function FinestraModale(_layer, _table, _tableBody, _buttonSalva, _buttonAnnulla) {
		this.layer = _layer;
		this.table = _table;
		this.tableBody = _tableBody;
		this.buttonSalva = _buttonSalva;
		this.buttonAnnulla = _buttonAnnulla;
		let self = this;

		this.buttonAnnulla.addEventListener("click", () => {
			self.reset();
			listaIscritti.refresh();
		});

		this.buttonSalva.addEventListener("click", () => {
			let multiVoto = [];
			Array.from(self.tableBody.children).forEach(tr => {
				let matricola = Array.from(tr.children)[0].textContent;
				let voto = Array.from(
					Array.from(
						Array.from(tr.children)[5] // cella contenente la select
							.children)[0] // select
						.children) // options
					.find(opt => opt.selected) // option selezionata
					.textContent;
				if (voto != "<vuoto>") {
					multiVoto.push({
						matricola: matricola,
						voto: voto
					});
				}
			});
			if (multiVoto.length > 0)
				self.modificaMultiVoto(multiVoto);
			else {
				self.reset();
				listaIscritti.refresh();
			}
		});

		// funzione per inviare al server i voti da inserire
		this.modificaMultiVoto = function(multiVoto) {
			let data = new FormData();
			data.append("profilo", "docente");
			data.append("azione", "modifica");
			data.append("nome_corso", self.nomeCorso);
			data.append("data_appello", self.dataAppello);
			data.append("matricola", "-1");
			data.append("multi_voto", JSON.stringify(multiVoto));
			makeCall("POST", "../EsitoEsame", data, request => {
				if (request.readyState == XMLHttpRequest.DONE) {
					if (request.status == 200) {
						self.reset();
						listaIscritti.refresh();
					} else if (request.status == 401) { // UNAUTHORIZED: utente non verificato
						window.location.href = "login_docente.html";
						window.sessionStorage.removeItem("docente");
					} else { // Errore nel server o nella richiesta
						sendError(request.status, request.responseText);
					}
				}
			});
		}

		// funzione per mostrare la finestra modale
		this.show = function(nomeCorso, dataAppello, tableBodyOrigine) {
			this.nomeCorso = nomeCorso;
			this.dataAppello = dataAppello;
			listaIscritti.hide();
			this.layer.style.display = "flex";
			Array.from(tableBodyOrigine.children)
				// ottengo solo righe con stato non inserito
				.filter(tr => Array.from(tr.children)[6].textContent == "non inserito")
				// per ognuna di esse creo una riga nella nuova tabella con stessi attributi
				.forEach(tr => {
					let row = document.createElement("tr");
					let cells = Array.from(tr.children);
					for (let i = 0; i < 5; i++) {
						let cell = document.createElement("td");
						cell.textContent = cells[i].textContent;
						row.appendChild(cell);
					}
					let selectCell = document.createElement("td");
					selectCell.textContent = "Voto: ";
					let select = document.createElement("select");
					voti.forEach(voto => {
						let opt = document.createElement("option");
						if (voto == "<vuoto>")
							opt.selected = true;
						opt.textContent = voto;
						select.appendChild(opt);
					})
					selectCell.appendChild(select);
					row.appendChild(selectCell);
					this.tableBody.appendChild(row);
				});
		}

		this.reset = function() {
			this.tableBody.innerHTML = "";
			this.layer.style.display = "none";
		}
	}

	// Form che mostra un verbale
	function VisualizzaVerbale(_wrapper, _table, _tableBody, _labels) {
		this.wrapper = _wrapper;
		this.table = _table;
		this.tableBody = _tableBody;
		this.labels = _labels;
		let self = this;

		// funzione che ottiene i dati del verbale dal server e mostra il verbale ottenuto
		this.show = function(codice) {
			makeCall("GET", "../Verbali?codice=" + codice, null, request => {
				if (request.readyState == XMLHttpRequest.DONE) {
					if (request.status == 200) {
						let pair = JSON.parse(request.responseText);
						self.updateData(pair.first, pair.second);
						self.wrapper.style.display = "block";
					} else if (request.status == 401) { // UNAUTHORIZED: utente non verificato
						window.location.href = "login_docente.html";
						window.sessionStorage.removeItem("docente");
					} else { // Errore nel server o nella richiesta
						sendError(request.status, request.responseText);
					}
				}
			});
		}

		// funzione che riempie il form e la tabelle veri e propri
		this.updateData = function(verbale, listaEsiti) {
			self.labels.codice.textContent = verbale.codice;
			self.labels.dataCreazione.textContent = verbale.dataCreazione;
			self.labels.oraCreazione.textContent = verbale.oraCreazione;
			self.labels.corso.textContent = verbale.nomeCorso;
			self.labels.data.textContent = verbale.dataAppello;
			listaEsiti.forEach(iscrizione => {
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
				self.tableBody.appendChild(row);
			});
		}

		this.reset = function() {
			self.wrapper.style.display = "none";
			self.tableBody.innerHTML = "";
		}
	}

	// Tabella che contiene tutti i verbali del docente
	function ListaVerbali(_wrapper, _table, _tableBody) {
		this.wrapper = _wrapper;
		this.table = _table;
		this.tableBody = _tableBody;
		let self = this;

		// funzione che ottiene i dati dal server e mostra la lista di verbali
		this.show = function() {
			makeCall("GET", "../Verbali", null, request => {
				if (request.readyState == XMLHttpRequest.DONE) {
					if (request.status == 200) {
						let listaVerbali = JSON.parse(request.responseText);
						self.updateData(listaVerbali);
						self.wrapper.style.display = "block";
					} else if (request.status == 401) { // UNAUTHORIZED: utente non verificato
						window.location.href = "login_docente.html";
						window.sessionStorage.removeItem("docente");
					} else { // Errore nel server o nella richiesta
						sendError(request.status, request.responseText);
					}
				}
			});
		}

		// funzione che crea la tabella vera e propria
		this.updateData = function(verbali) {
			verbali.forEach(verbale => {
				let row = document.createElement("tr");
				let codiceCell = document.createElement("td");
				codiceCell.textContent = verbale.codice;
				row.appendChild(codiceCell);
				let corsoCell = document.createElement("td");
				corsoCell.textContent = verbale.nomeCorso;
				row.appendChild(corsoCell);
				let dataCell = document.createElement("td");
				dataCell.textContent = verbale.dataAppello;
				row.appendChild(dataCell);
				let dataCreazCell = document.createElement("td");
				dataCreazCell.textContent = verbale.dataCreazione;
				row.appendChild(dataCreazCell);
				let oraCreazCell = document.createElement("td");
				oraCreazCell.textContent = verbale.oraCreazione;
				row.appendChild(oraCreazCell);
				let linkCell = document.createElement("td");
				let linkImage = document.createElement("img");
				linkImage.src = "../imgs/goto.png"
				linkImage.className = "goto-icon";
				linkCell.setAttribute("codice", verbale.codice);
				linkImage.addEventListener("click", (evt) => {
					self.reset();
					buttonIndietro.evolveVerbale();
					visualizzaVerbale.show(evt.target.closest("td").getAttribute("codice"));
				});
				linkCell.appendChild(linkImage);
				row.appendChild(linkCell);
				self.tableBody.appendChild(row);
			});
		}

		// funzione che resetta e nasconde il componente
		this.reset = function() {
			self.wrapper.style.display = "none";
			self.tableBody.innerHTML = "";
		}
	}
































}