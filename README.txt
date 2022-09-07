README
Di Gallo Matteo 830703 m.digallo@campus.unimib.it

Per compilare è richiesto:
    inserire il comando "mvn package" nella console
Per eseguire è richiesto:
    java -jar target/demo-0.0.1-SNAPSHOT.jar

In alternativa caricarlo ed eseguirlo su IntelliJIdea.
Versione di Java usata 17.

Una volta compilato ed eseguito il progetto usare un browser su "localhost:8080/agenda.html"
Inizialmente si verrà accolti da una schermata che propone la scelta fra "login" e "registrazione".
Nel caso non si disponga di un username e password e' necessrio registrarsi.
Una volta ottenuto l'accesso l'utente vedrà nel menù in alto le opzioni per vedere tutti gli elementi del database:
Agenda contiene un elenco ordinato di tutte le singole task fissate con orario di inizio e fine.
Queste sono create da:
	Impegni: ovvero attività ricorrenti provviste di una durata, un preavviso e una frequenza con cui vengono ripetute dalla data di inizio a quella di fine.
	Attività: azioni che non si ripetono, da svolgere in un certo lasso di tempo pianificandole nei periodi non occupati da altri eventi
	le attivit° non pianificate di un utente vengono classificate con una priorità assegnata in base alla data di scadenza: questa informazione é mantenuta aggiornata da un
	trigger definito sul database per ogni modifica dei dati della tabella ATTIVITA.
Le date di inizio, preavviso e fine di un evento possono essere spostate a piacimento dall'utente mentre il titolo e il tipo sono ereditati dall'impegno o dall'attività da cui sono stati derivati.
I tipi di attività consentono di scegliere una tipologia di evento, come lo sport lo studio, lo svago, il lavoro, e un relativo colore per renderli più facilmente identificabili.
Ogni elemento dell'agenda, attività, impegno o tipo possono essere eliminati, modificati, creati (tranne gli elementi dell'agenda non creabili direttamente) e filtrabili.
Il Filtro è ottenuto premendo sul tasto new che consente di inserire i criteri di ricerca (ad es. titolo, tipo, date inzio e fine).
E' possibile visualizzare gli eventi derivati da un impegno (bottone "Agenda) o da una attivia' che sia pianificata.
Quando é attva la lista delle attvità Attività dono presenti diverse funzioni uniche quali:
	Suggerisici: che individua l'attività più "urgente"(ovvero che scade per prima) da eseguire anche immediatamente nel caso l'utente decida di impiegare il suo tempo libero
	Pianifica: permette di fissare sull'agenda un'attività scegliendo manualmente una istante di inizio
	Ottimizza: data una data di inizio e una di fine, restituisce la lista con il massimo numero di attività non ancora pianificate che é possibile eseguire nel periodo.
	           Il programma determina gli intervalli di tempo liberi da eventi già in agenda nel periodo indicato e propone le attivit° a partire dalla piu' breve al
	           fine di massimizzare il numero deòòe attività.
	           Cnsiglio di inserire una impegno "riposo" la notte altrimenti il database supporrà che siate in grado di rimanere svegli 24/7)
Inoltre è presente una rubrica con la possibilità di creare, cercare, modificare ed eliminare i propri contatti con mail e numero di telefono e di catalogarli in tipologie come per gli eventi e le attività.

Le seguenti funzionalita' dell'interfaccia meritano una descrizione:
 - clickando su una riga di una lista i suoi dati vengono presentati su una form
 - clickando su altre righe della lista la form viene aggiornaa
 - la form pu^o essere chiusa con dblClkick
 Le funzioni vengono eseguite immediatamente senza richieste i conferma

Lo soluzione é basata su
- Database SQL DerbyDB (distribuito da Apache foundation) con constraint di inegrità referenziale (foregn keys) e trigger per il calcolo della priorità e della durata delle attività
- Applicatzione in Java Spring che espone servitzi HTTP
- Applicatione server Tomcat
- Front end in HTML e Javascript codificati manualmente com chamate XMLHTTPRequest


Compilato, eseguito e testato su un pc con Windows 10 e Mac.