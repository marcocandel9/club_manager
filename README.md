# ClubManager
Software for the management of a Club. Includes features like adding new members, new books, renting and unrenting of books, listing of members and books. It implements a client/server database(MongoDB) connection.
Documento di progetto – Gestore Club
Marco Candela (515472)

Indice
1.	Introduzione
2.	Requisiti di Progetto
2.1.	Programmazione ad oggetti
2.1.1.	 Panoramica Classi
2.2.	Information Hiding
2.3.	Ereditarietà
2.4.	Polimorfismo
2.5.	Multithreading
2.6.	Gestione eccezioni
2.7.	File di configurazione XML
2.8.	Gestione del Database
2.9.	Connessione Client/Server


1 Introduzione
il progetto è stato realizzato per la gestione dell'apertura di una nuova associazione nella città di Messina. Le funzionalità della prima versione del programma prevedono la gestione dell'iscrizione di nuovi membri e il noleggio di libri per chi fa parte del Club.
L’intero codice è reperibile all’interno della repository GitHub
Le funzionalità che implementa la prima versione del progetto prevedono: 
1	Aggiunta e quindi iscrizione di nuovi membri
2	Rimozione di membri già iscritti
3	L'aggiunta di un nuovo libro
4	La rimozione di un libro preesistente 
5	Il noleggio di un libro preesistente
6	La restituzione di un libro noleggiato
7	La visualizzazione dell'elenco membri
8	La visualizzazione dell'elenco libri
Il sistema di gestione è stato implementato utilizzando Java, strutturato come un progetto Maven, e interfacciato con un database MongoDB. La scelta di MongoDB, un database NoSQL, è stata guidata dalla sua natura non relazionale, che si è rivelata ottimale per le necessità del progetto. 
2 Requisiti di Progetto

2.1 Programmazione ad Oggetti
Il progetto è stato interamente realizzato con un approccio orientato alla Programmazione Oggetti. Sono presenti 4 classi: Libro, Persona, Libro Noleggiato e Membro. Le ultime due estendono rispettivamente le prime due classi.

2.1.1 Panoramica Classi 
La classe Persona contiene variabili private che identificano un'entità di tipo persona, garantendo un approccio in linea con il principio dell'Information Hiding. È stata definita la variabile fiscalCodeId per assicurare un identificatore unico, fungendo da super-chiave per differenziare gli oggetti di tipo Membro all'interno del Database.
La classe Membro estende la classe Persona. Tramite la libreria Java.time vengono istanziate dalla classe ulteriori 2 variabili private che aggiungono informazione sulla data di registrazione all'associazione e la data di fine. La durata dell'iscrizione per specifica di progetto ha una durata di un anno.
La classe LibroNoleggiato estende la classe Libro. Arricchisce la classe con variabili private che aggiungono informazioni riguardanti il noleggio del libro:
•	Data di inizio noleggio, implementata tramite la libreria java.time
•	La caparra data dal noleggiante per il noleggio del libro
•	Il nome e cognome del noleggiante
•	Il codice fiscale del noleggiante
Le funzionalità  effettive del gestore sono implementate da due classi, ‘GestoreClient’ e ‘GestoreDB’. Si utilizzano queste due classi distinte per compartimentare l’I/O col client e l’accesso al database effettivo. Per illustrare questo, prendiamo in esame la funzionalità "aggiunta di un libro" attraverso il metodo addBook.
Il metodo addBook() si occuperà di effettuare il flusso di dati I/O dal client al server, salverà i dati richiesti per la creazione di un oggetto libro all’interno di appositi campi che poi passerà al costruttore della classe Libro. Il metodo addBook(Libro libro) del gestoreDB verrà invocato passando come parametro il nuovo libro creato con i dati richiesti dall’utente.

    public void addBook() throws Exception {
        try {
            out.println("Inserisci codice ISBN del libro:"); //valore identificativo unico di ogni libro
            out.println(ASK);
            String isbn = in.readLine();
            out.println("Inserisci il titolo del libro da aggiungere:");
            out.println(ASK);
            String nomeLibro = in.readLine();
            out.println("Inserisci l'autore del libro: ");
            out.println(ASK);
            String autoreLibro = in.readLine();
            out.println("Inserisci il nome e cognome del proprietario del libro");
            out.println(ASK);
            String fullNameOwner = in.readLine();
            out.println("Inserisci il costo del libro");
            out.println(ASK);
            Double prezzoLibro = Double.parseDouble(in.readLine());
            Libro nuovoLibro = new Libro(isbn, nomeLibro, autoreLibro, prezzoLibro, fullNameOwner); //creazione dell'oggetto libro
            //qui potrebbe essere implementata una logica di accesso a un arrayList locale di libri

            gestoreDB.addBook(nuovoLibro); //chiamata al metodo addBook del gestore DB
        } catch (Exception e) {
            out.println("Si è verificato un errore nell'aggiunta del libro. Riprovare. " + e.getMessage());
        }
    }

Di seguito vi si mostra l’implementazione del metodo all’interno del gestoreDB il quale avrà il compito dell’effettiva aggiunta del libro all’interno del Database.
        public void addBook(Libro libro) throws Exception {
                try {
                        Document newBook = new Document()
                                .append("_id", libro.getIsbn())
                                .append("isRented", libro.getRented())
                                .append("titolo", libro.getTitolo())
                                .append("autore", libro.getAutore())
                                .append("prezzoLibro", libro.getPrezzo())
                                .append("proprietarioLibro", libro.getProprietario())
                                .append("noleggio", new Document("caparra", null)  //con questa query viene inserito l'oggetto noleggio 
                                        .append("renterName", null)
                                        .append("renterFiscalCodeId", null)
                                        .append("rentStartDate", null));

                        System.out.println(newBook);
                        tabLibri.insertOne(newBook);

                        System.out.println("Libro aggiunto alla collezione Libri del db");

                } catch (Exception e) {
                        throw new Exception("Errore durante l'aggiunta del libro: " + e.getMessage());
                }
        }

Si noti come i campi relativi al noleggio del libro vengano istanziati a “null”, per essere preparati ad essere eventualmente riempiti grazie ai metodi rentBook(), presenti all’interno dei gestori Client e DB.

2.2 Information Hiding
L’Information Hiding è stato realizzato creando all’interno delle classi delle variabili private accessibili
solo grazie ai metodi della classe stessa. Un esempio viene mostrato di seguito.
	public class Membro extends Persona{

    private LocalDate dataInizioIscrizione;
    private LocalDate dataFineIscrizione;


I campi private sono rispettivamente accessibili e modificabili solo tramite i metodi getter e setter elencati di seguito: 
 

	   public LocalDate getDataInizioIscrizione() {
        return dataInizioIscrizione;
    }

    public void setDataInizioIscrizione(LocalDate dataInizioIscrizione) {
        this.dataInizioIscrizione = dataInizioIscrizione;
    }

    public LocalDate getDataFineIscrizione() {
        return dataFineIscrizione;
    }

    public void setDataFineIscrizione(LocalDate dataFineIscrizione) {
        this.dataFineIscrizione = dataFineIscrizione;
    }
}

2.3 Ereditarietà 
Grazie ai costruttori delle classi Membro e LibroNoleggiato vengono correttamente implementati i concetti orientati alla programmazione ad oggetti dell’ereditarietà e dell’incapsulamento.
    // Il costruttore di Membro invoca il costruttore della classe padre, Persona, e inizializza le date di iscrizione.    
public Membro(String fiscalCodeId, String nome, String cognome, int eta, String email, String phoneNum) {
        super(fiscalCodeId, nome, cognome, eta,email,phoneNum);
        this.dataInizioIscrizione = LocalDate.now();
        this.dataFineIscrizione = this.dataInizioIscrizione.plusYears(1);

    }

La classe membro ad esempio eredita la superclasse Persona, nel suo costruttore invoca il rispettivo costruttore della superclasse. 

2.4 Polimorfismo
Il polimorfismo, in particolare il polimorfismo di override, è stato implementato sovrascrivendo il metodo `run()` per gestire il multithreading. Gli oggetti di tipo `GestoreClient` possono essere utilizzati come oggetti di tipo `Runnable` grazie a questa sovrascrittura. Questa implementazione consente una gestione più flessibile e versatile dei thread nel sistema.   
    @Override
    public void run() {
        while (true) {
            try {
                clientStart(); //nel clientStart è stato inizializzato un ciclo while che termina solo quando il client decide di uscirne con l'apposito comando.
            } catch (Exception e) { //qualsiasi tipo di eccezione arrivi dal ClientStart verra' gestita
                continue;
            }
            break; //se non ci sono eccezioni deve uscire
        }
    }

2.5 Multithreading
Il multithreading nel progetto è stato realizzato implementando l'interfaccia Runnable. Nel main del server, un ciclo while permette al server di restare in ascolto e di attendere connessioni dai client. Ad ogni connessione da parte di un client, viene creato un nuovo oggetto GestoreClient, al quale vengono passati il socket di comunicazione e l'oggetto gestoreDB, quest'ultimo inizializzato al di fuori del ciclo while. Questo approccio consente la creazione di un nuovo oggetto GestoreClient ogni volta che un nuovo client stabilisce una connessione con il server.

La classe GestoreClient implementa l'interfaccia Runnable, permettendo così di passare l'oggetto al costruttore di Thread e di eseguire il metodo run() in un thread separato, come illustrato di seguito:
       
	 while(!esci){
            System.out.println("Il server è in ascolto.");
            socket = serverSocket.accept();
            System.out.println("E' arrivato un cliente");
            gestoreClient = new GestoreClient(socket,gestoreDB);
            thread = new Thread(gestoreClient);
            thread.start();
        }
Di seguito il metodo run() all’interno della classe gestoreClient:
    @Override
    public void run() {
        while (true) {
            try {
                clientStart(); //nel clientStart è stato inizializzato un ciclo while che termina solo quando il client decide di uscirne con l'apposito comando.
            } catch (Exception e) { //qualsiasi tipo di eccezione arrivi dal ClientStart verra' gestita
                continue;
            }
            break; //se non ci sono eccezioni deve uscire
        }
    }

2.6 Gestione eccezioni
Le gestioni vengono gestite in ogni livello del flusso di controllo del programma. I metodi del GestoreDB propagano l’eccezione al metodo gestoreClient, i quali vengono invocati dal metodo clientStart della stessa classe gestore Client, a sua volta lanciato dal metodo Run(). L’eccezione partita dal GestoreDB verrà propagata fino al metodo Run(), il quale mostrerà quella determinata eccezione all’interno del terminale del client.
Di seguito verranno mostrati i codici dei metodi sopra elencati, utilizzando come esempio il lancio del metodo removeBook(String isbn) da parte del gestoreClient.

    @Override
    public void run() {
        while (true) {
            try {
                clientStart(); //nel clientStart è stato inizializzato un ciclo while che termina solo quando il client decide di uscirne con l'apposito comando.
            } catch (Exception e) { //qualsiasi tipo di eccezione arrivi dal ClientStart verra' gestita
                continue;
            }
            break; //se non ci sono eccezioni deve uscire
        }
    }

Nel metodo clientStart(), l'eccezione è lanciata quando si verifica un errore durante l'invocazione del metodo removeBook(), che a sua volta chiama il metodo removeBook(String isbn) in GestoreDB. Se un libro con un dato ISBN non è presente nel database, un'eccezione viene lanciata e propagata fino al metodo run(), e il messaggio d'errore è visualizzato nel terminale del client:

    public void clientStart() throws Exception {
        Scanner keyboard = new Scanner(System.in);
        int rispostaClient, domandaServer;
        boolean esci = false; //flag esci che viene impostata nello switch case a true quando il client vuole terminare la comunciazione
        out.println("Benvenuto!");
        while (!esci) {
            out.println("A quale funzione vuoi accedere?" + "\n1) Aggiungi membro" + "\n2) Rimuovi membro" + "\n3) Aggiungi libro" + "\n4)Rimuovi libro " + "\n5) Noleggia libro" + "\n6) Cancella noleggio" + "\n7) Mostra lista membri" + "\n8) Mostra lista libri" + "\n 9) Esci");
            out.println(ASK); //il server vuole una risposta
            rispostaClient = Integer.parseInt(in.readLine());
            switch (rispostaClient) {
                case 1 -> addMember();
                case 2 -> removeMember();
                case 3 -> addBook();
                case 4 -> removeBook();
                case 5 -> rentBook();
                case 6 -> unrentBook();
                case 7 -> showMembers();
                case 8 -> showBooks();
                case 9 -> esci = true;
                default -> {
                    out.println("Risposta non valida, riprovare. \n");
                    break;
                }
            }
        }
        out.println(CLOSE); //chiusura della comunicazione da parte del server a seguito della volontà di chiusura del client
        in.close();
        out.close();
        socket.close();
    }



    public void removeBook() throws Exception {
        try {
            out.println("Inserisci codice ISBN del libro da rimuovere:"); //valore identificativo unico di ogni libro
            String isbn = in.readLine();
            gestoreDB.removeBook(isbn); //chiamata al metodo removeBook del gestore DB

        } catch (Exception e) {
            out.println("Si è verificato un errore nella rimozione del libro. Riprovare. " + e.getMessage());
        }
    }




                public void removeBook(String isbn) throws Exception {
                try {
                        //blocco di query nel database per il libro da eliminare
                        Document queryBook = new Document("_id", isbn); //documento utile a effettuare la query
                        Document fetchedBook = tabLibri.find(queryBook).first(); //metodo find(query) per trovare e restituire il documento
                        if (fetchedBook != null) {    //blocco condizionale che controlla che il libro sia presente nel database
                                System.out.println("Libro trovato." + fetchedBook.toJson());
                        } else {
                                throw new Exception("Libro non presente nel database.");
                        }

                        //blocco di rimozione libro dal database
                        tabLibri.findOneAndDelete(fetchedBook); //utilizza il documento fetchedBook di cui si è fatta la query precedentemente
                        System.out.println("Il libro è stato correttamente rimosso dal database.");

                } catch (Exception e) {
                        throw new Exception("Errore durante la rimozione del libro: " + e.getMessage());
                }
        }

Seguendo il flusso della propagazione dell’errore, questo è l’output nel terminale del Client nel momento in cui, ad esempio, viene inserito un codice ISBN identificativo di un libro non presente all’interno del database.
Inserisci il codice ISBN Identificativo del libro
12345
Si è verificato un errore col termine del noleggio. Riprovare. Errore durante la cancellazione del noleggio: Libro non presente nel database.

2.7 File di configurazione XML
Sono stati usati 3 file di configurazione XML, un primo file xml chiamato pom.xml, generato dal
progetto Maven, e stato utilizzato per impostare le dipendenze sia con Telegram che con MongoDB.

Dipendenze:

<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>org.example</groupId>
  <artifactId>Gestione_Club</artifactId>
  <version>1.0-SNAPSHOT</version>
  <packaging>jar</packaging>

  <name>Gestione_Club</name>
  <url>http://maven.apache.org</url>

  <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <maven.compiler.source>19</maven.compiler.source>
    <maven.compiler.target>19</maven.compiler.target>
  </properties>

  <dependencies>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>3.8.1</version>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>org.mongodb</groupId>
      <artifactId>mongodb-driver-sync</artifactId>
      <version>4.10.1</version>
    </dependency>
  </dependencies>
</project>

Il file clientConfig contiene la configurazione specifica per il client, includendo l'indirizzo IP e la porta del server a cui il client si connetterà. 

File clientConfig
<client>
    <ip>localhost</ip>
    <port>12345</port>
</client>


Il file serverConfig include le configurazioni per il server e il database, specificando la porta del server l’URI del database MongoDB, oltre al nome del database.

File serverConfig

<config>
    <db>
        <uri>mongodb://localhost:27017</uri>
        <dbname>ClubDatabase</dbname>
    </db>
    <server>
        <port>12345</port>
    </server>
</config>


2.8 Gestione del Database
Il database è implementato attraverso MongoDB e l’utilizzo del suo driver Java. Le funzionalità che si occupano di realizzare le query, di aggiungere, eliminare o modificare informazioni all’interno del database vengono gestite nella classe GestoreDB. 
 
I dati gestiti all’interno del database sono strutturati in due collezioni distinte
•	Libri
•	Membri
I membri utilizzano come super-chiave identificativa il codice fiscale, inserita nel campo _id standard di mongoDB.
I libri utilizzano come super-chiave identificativa il codice ISBN, inserita anch’essa nel campo _id standard.
Per quanto riguarda i documenti libri, essi posseggono un campo isRented che specifica se siano o meno attualmente noleggiati. Nel caso in cui un libro venga noleggiato, tramite le funzionalità implementate dal gestoreClient e dal gestoreDB è possibile inserire i dati del noleggiante all’interno del sotto-documento noleggio. Quest’ultimo prevede l’impostazione di tutti i suoi valori a null nel caso in cui il libro non sia attualmente noleggiato. 
Di seguito un esempio di due libri appartenenti alla collezione Libro, di cui il primo non noleggiato ed il secondo sì.
 



2.9 Connessione Client / Server
Client e server sfruttano un protocollo TCP per la comunicazione. Il server inizializza l’ascolto utilizzando la classe ServerSocket e il cliente, nel momento in cui tenta di intraprendere una comunicazione, viene accettato tramite la chiamata bloccare Socket.accept(). La gestione del client è gestita dalla classe GestoreClient, la quale implementa i menù di avvio e le funzionalità. 
Di seguito il codice della classe Server: 
public class Server {
    public static void main (String [] args) throws Exception {
        ServerSocket serverSocket;
        Socket socket;
        PrintWriter out;
        BufferedReader in;
        File file = new File("C:\\Users\\Marco Candela\\Desktop\\Gestionale Club\\Gestione_Club\\src\\main\\java\\gestoreClub\\serverConfig");
        String uri;
        String dbname;
        int port;
        boolean esci = false;


        //parsing di uri e port dal file XML utilizzando la classe ParserXML
        ParserXML DbParser = new ParserXML(file);
        Element root = DbParser.ElementRootCatch();
        //navigo nel file XML dal root element per selezionare l'elemento uri ed estrarne l'attributo
        uri = root.getElementsByTagName("uri").item(0).getTextContent();

        // Navigo nel file XML per ottenere il nome del database
        Node dbNode = root.getElementsByTagName("db").item(0);
        Element dbElement = (Element) dbNode;
        dbname = dbElement.getElementsByTagName("dbname").item(0).getTextContent();

        //navigo nel file XML dal root element per selezionare l'elemento port ed estrarne l'attributo
        port = Integer.parseInt(root.getElementsByTagName("port").item(0).getTextContent());


        GestoreClient gestoreClient;
        Thread thread;

        serverSocket = new ServerSocket(port);
        GestoreDB gestoreDB = new GestoreDB(uri,dbname); 


        while(!esci){
            System.out.println("Il server è in ascolto.");
            socket = serverSocket.accept();
            System.out.println("E' arrivato un cliente");
            gestoreClient = new GestoreClient(socket,gestoreDB);
            thread = new Thread(gestoreClient);
            thread.start();
        }

    }
}




 





I dati del database vengono reperiti tramite il ParsingXML dei file config, come mostrato nella sezione 2.7. 
Un solo gestoreDB viene istanziato per tutti i client che vorranno mettersi in comunicazione col server.
Più GestoriClient verranno invece istanziati, uno per ogni client.
La classe Client utilizza la classe Socket per la comunicazione e le classi BufferedReader e PrintWriter per scrivere e leggere.
L’IP e la porta passate al Socket vengono in questo caso passate tramite parsingXML del file clientConfig. 
Si noti come il client sia costantemente in ascolto. Vengono inoltre utilizzate le costanti ASK e CLOSE che, inviate dal server e lette tramite BufferedRead, indicano rispettivamente che il server necessiti di una risposta o di chiudere la connessione, permettono al client di comportarsi di conseguenza.
public class Client {

    private static final String CLOSE = "!"; //Carattere che indica al client di voler chiudere la connessione
    private static final String ASK = "?"; // //carattere che indica al client di volere una risposta

    public static void main (String [] args) throws Exception {
        Socket socket;
        PrintWriter out;
        BufferedReader in;
        String ip;
        int port;
        ParserXML ClientParser;
        Element root;
        File file = new File("C:\\Users\\Marco Candela\\Desktop\\Gestionale Club\\Gestione_Club\\src\\main\\java\\gestoreClub\\clientConfig.xml");
        Scanner input = new Scanner(System.in);
        String rispostaClient, domandaServer;

        //parser file config client
        ClientParser = new ParserXML(file);
        root = ClientParser.ElementRootCatch();
        //parse del config client per prendere il port e cast a Integer
        port = Integer.parseInt((root.getElementsByTagName("port").item(0)).getTextContent());
        System.out.println(port);
        //parse del config client per prendere l'ip
        ip = root.getElementsByTagName("ip").item(0).getTextContent();
        System.out.println(ip);




        socket = new Socket(ip, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        //il client comunica con il server leggendo sempre cio' che scrive fin quando la connessione non viene chiusa
        while (true) {
            domandaServer = in.readLine(); //legge l'input che arriva dal server e lo salva nella string domandaServer
            if(domandaServer.equals(CLOSE)) //il server vuole smettere di comunicare
                break;
            else if(domandaServer.equals(ASK)) { //il server vuole una risposta
                rispostaClient = input.nextLine();
                out.println(rispostaClient);
            }
            else //stampo la domanda
                System.out.println(domandaServer);
        }
        //chiusura comunicazione
        in.close();
        out.close();
        socket.close();
    }

}


