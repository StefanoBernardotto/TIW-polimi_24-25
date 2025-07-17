/**
 * Script per la home page dello studente
 */
{
	// Componenti della pagina
	let labelNome;
	
	// Oggetto che contiene lo studente loggato
	let studente;

	// verifica se l'utente è loggato, se non lo è rimanda alla pagina di login
	if (sessionStorage.getItem("studente") == null) {
		window.location.href = "../index.html";
	}else{
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
		}
		this.refreshPage = function(){
			labelNome.show()
		}
	}

	function NomeStudente(_nomeCognome, labelNomeTopBar) {
		this.nomeCognome = _nomeCognome;
		this.show = function() {
			labelNomeTopBar.textContent = this.nomeCognome;
		}
	}
}