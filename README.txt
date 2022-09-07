README
Di Gallo Matteo 830703 m.digallo@campus.unimib.it

Per compilare è richiesto

Una volta compilato ed eseguito il progetto usare un browser su "localhost:8080/agenda.html"
Inizialmente si verrà accolti da una schermata che propone la scelta fra "login" e "registrazione".
Nel caso non si disponga di un username e password conviene registrarsi prima, ma non sarà possibile cambiare nominativo e password.
Una volta ottenuto l'accesso l'utente vedrà nel menù in alto a sx le opzioni per vedere tutti gli elementi del database:
Agenda contiene un elenco ordinato di tutte le singole task fissate con orario di inizio e fine.
QUeste sono create da:
	Impegni: ovvero attività ricorrenti provviste di una durata, un preavviso e una frequenza con cui vengono ripetute dalla data di inizio a quella di fine.
	Attività: azioni che non si ripetono da svolgere in un certo lasso di tempo pianificandole qualora si abbia un buco libero fra gli eventi
Le date di inizio, preavviso e fine di un evento possono essere spostate a piacimento dall'utente mentre il titolo e il tipo sono ereditati dall'impegno o dall'attività da cui sono stati creati.
I tipi di attività consentono di scegliere una tipologia di evento, come lo sport lo studo, lo svago, il lavoro, e un relativo colore per renderli più gradevoli alla vista.
Ogni elemento dell'agenda, attività, impegno o tipo possono essere eliminati o modificati, creati(tranne gli elementi dell'agenda non creabili direttamente) e filtrabili. Il Filtro è ottenuto premendo sul tasto new che consente di ricercare per titolo e tipo e nel caso degli eventi per impegno e attività creatrici e per intervalli di tempo.
Attività presenta diverse funzioni uniche quali:
	Suggerisici che individua l'attività più "urgente"(ovvero che scade per prima) da eseguire anche immediatamente nel caso l'utente decida di impiegare il suo tempo libero
	Pianifica: permette di fissare sull'agenda un'attività scegliendo manualmente una data di inizio
	Ottimizza: data una data di inizio e una di fine, restituisce una lista di tutte le possibili attività non pianificate da eseguirsi una in fila all'altra fra un evento e l'altro(consiglio di inserire una impegno "riposo" la notte altrimenti il database supporrà che siate in grado di rimanere svegli 24/7)
Inoltre è presente una rubrica con la possibilità di creare, cercare, modificare ed eliminare i propri contatti con mail e numero di telefono e di catalogarli in tipologie come per gli eventi e le attività.
