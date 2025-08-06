# Progetto TIW - Politecnico di Milano

## Introduzione

Questo progetto fa parte del corso **Tecnologie Informatiche per il Web (TIW)** presso il Politecnico di Milano.
Il progetto consiste nella realizzazione di due applicazioni distinte che rispondono alle medesime specifiche: la prima in versione pure HTML, la seconda in versione RIA (Rich Internet Application).

### Istruzioni per l'uso - Eseguire il progetto localmente

Il progetto non è stato deployato, è possibile eseguirlo localmente seguendo questi passaggi:

1. Assicurarsi di avere installato Java 21, MySQL e Apache Tomcat 10.
2. Clonare il repository:

    ```bash
    git clone https://github.com/StefanoBernardotto/TIW-polimi_24-25.git
    ```

3. Importare il progetto in Eclipse come progetto esistente.
4. Importare il database MySQL utilizzando il file `dump-only-iscrizioni.sql`.
5. Click destro sul progetto in Eclipse, selezionare "Run As" e poi "Run on Server" e Apache Tomcat 10.
6. Aprire il browser e navigare all'indirizzo `http://localhost:8080/TIW-proj-esami/`.

### Istruizioni per l'uso - Importare il progetto su IntelliJ IDEA

Per importare il progetto su IntelliJ IDEA (o qualunque altro IDE che supporta Maven), seguire questi passaggi:

1. Assicurarsi di avere installato Java 21, MySQL e Apache Tomcat 10.
2. Clonare il repository:

    ```bash
    git clone https://github.com/StefanoBernardotto/TIW-polimi_24-25.git
    ```

3. Importare il progetto in Eclipse come progetto esistente.
4. Importare il database MySQL utilizzando il file `dump-only-iscrizioni.sql`.
5. Click destro sul progetto in Eclipse, selezionare "Configure" e poi "Convert to Maven Project"
6. Importare il progetto in IntelliJ IDEA come progetto Maven e fare refresh delle dipendenze.

## Tecnologie

Il progetto utilizza le seguenti tecnologie:

- Java 21 (Backend)
- HTML, CSS, JavaScript (Frontend)
- MySQL (Database)
- Apache Tomcat 10 (Server)

## Strumenti di sviluppo

Per lo sviluppo del progetto si sono utilizzati:

- Eclipse
- MySQL Workbench e DataGrip
- Draw.io e Visual Paradigm

## Valutazioni e considerazioni del docente
**Valutazione**: 27 / 30L.

Il docente ha indicato i seguenti punti critici nella progettazione e nell'implementazione dell'applicazione:

- **Pagina "Scelta profilo"**: superflua, il login deve essere unico sia per docente che per studente, in base alle credenziali il sito reindirizza alla pagina corretta; dovrebbe essere inserito anche il ruolo dell'utente nel database per identificarlo chiaramente
- **Servlet e URL**: i controllori sono mal progettati, è importante avere un controllore per ogni "azione" che si vuole implementare; il docente ha ritenuto errato l'utilizzo di parole che identificano il comando da eseguire (es "modifica" / "rifiuta"), richiede piuttosto che vengano utilizzati più controllori
- **Iscrizioni, corsi e appelli**: il docente ha sottolineato che l'uso del nome come identificativo per un corso è errato (si pensi ad un corso diviso in più sezioni con più docenti), così come ha sottolineato la necessità di un codice identificativo per l'iscrizione ad un esame piuttosto che identificare la stessa con un chiave composta da tre campi

## Autore

- Bernardotto Stefano (<https://github.com/StefanoBernardotto>): studente di Ingegneria Informatica

## Licenza

Il contenuto di questa repository (eccetto il file "Specifica.pdf") è di proprietà di Bernardotto Stefano e non deve essere riprodotto.
