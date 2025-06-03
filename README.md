# ClubManager

**ClubManager** è un software in Java per la gestione di un'associazione culturale, con funzionalità di iscrizione membri, gestione dei libri e noleggio. Il sistema utilizza un'architettura client-server con connessione a un database MongoDB.

## Autore

Marco Candela  
Matricola: 515472

## Indice

- [Introduzione](#introduzione)
- [Requisiti e Tecnologie](#requisiti-e-tecnologie)
  - [Programmazione a Oggetti](#programmazione-a-oggetti)
  - [Information Hiding](#information-hiding)
  - [Ereditarietà e Polimorfismo](#ereditarietà-e-polimorfismo)
  - [Multithreading](#multithreading)
  - [Gestione delle Eccezioni](#gestione-delle-eccezioni)
  - [File di Configurazione XML](#file-di-configurazione-xml)
  - [Gestione del Database](#gestione-del-database)
  - [Connessione Client/Server](#connessione-clientserver)
- [Funzionalità](#funzionalità)
- [Esempio: Aggiunta Libro](#esempio-aggiunta-libro)

## Introduzione

Il progetto nasce per supportare la gestione di un nuovo Club nella città di Messina. La prima versione dell'applicativo consente la gestione dei membri e dei libri tramite un'interfaccia client che comunica con un server Java connesso a un database MongoDB.

## Requisiti e Tecnologie

### Programmazione a Oggetti

Il sistema è strutturato secondo i principi OOP. Le principali classi sono:

- `Persona`: contiene dati anagrafici con identificatore univoco `fiscalCodeId`.
- `Membro`: estende `Persona`, aggiungendo data di inizio e fine iscrizione.
- `Libro`: definisce le proprietà di un libro.
- `LibroNoleggiato`: estende `Libro`, aggiungendo dettagli sul noleggio.

Le funzionalità sono gestite da due classi principali:

- `GestoreClient`: gestisce la comunicazione con il client.
- `GestoreDB`: gestisce le operazioni sul database MongoDB.

### Information Hiding

Tutti gli attributi delle classi sono `private`, e l'accesso avviene tramite metodi `getter` e `setter`.

### Ereditarietà e Polimorfismo

Le classi `Membro` e `LibroNoleggiato` estendono rispettivamente `Persona` e `Libro`. È stato implementato il polimorfismo di override nel metodo `run()` per la gestione dei thread.

### Multithreading

Il server crea un nuovo `Thread` per ogni connessione client, istanziando un nuovo oggetto `GestoreClient` che implementa `Runnable`.

### Gestione delle Eccezioni

Le eccezioni vengono propagate lungo il flusso di esecuzione, dalla classe `GestoreDB` fino al metodo `run()` del client handler. Ogni errore è gestito con messaggi chiari inviati al client.

### File di Configurazione XML

Sono utilizzati tre file XML:

- `pom.xml`: gestisce le dipendenze Maven, incluse quelle per MongoDB.
- `clientConfig.xml`: contiene IP e porta del server.
- `serverConfig.xml`: contiene URI MongoDB, nome del database e porta.

### Gestione del Database

Il database MongoDB contiene due collezioni:

- `Libri`: identificati dall'`ISBN`, campo `_id`.
- `Membri`: identificati dal codice fiscale, campo `_id`.

Ogni libro ha un campo `isRented` e un sottodocumento `noleggio` contenente:
- Nome noleggiante
- Codice fiscale
- Data inizio noleggio
- Caparra

### Connessione Client/Server

La comunicazione avviene tramite protocollo TCP. Il server utilizza `ServerSocket` per accettare connessioni, mentre il client si connette tramite `Socket`. Lo scambio di messaggi avviene con `BufferedReader` e `PrintWriter`.

## Funzionalità

1. Iscrizione di nuovi membri
2. Rimozione di membri
3. Aggiunta di nuovi libri
4. Rimozione di libri
5. Noleggio libri
6. Restituzione libri
7. Visualizzazione elenco membri
8. Visualizzazione elenco libri

## Esempio: Aggiunta Libro

Nel `GestoreClient`, il metodo `addBook()` acquisisce i dati tramite I/O e crea un oggetto `Libro`, passato a `GestoreDB.addBook()`:

```java
public void addBook() throws Exception {
    try {
        out.println("Inserisci codice ISBN del libro:");
        out.println(ASK);
        String isbn = in.readLine();
        out.println("Inserisci il titolo del libro:");
        out.println(ASK);
        String nomeLibro = in.readLine();
        out.println("Inserisci l'autore:");
        out.println(ASK);
        String autoreLibro = in.readLine();
        out.println("Inserisci nome e cognome del proprietario:");
        out.println(ASK);
        String fullNameOwner = in.readLine();
        out.println("Inserisci il prezzo:");
        out.println(ASK);
        Double prezzo = Double.parseDouble(in.readLine());

        Libro nuovoLibro = new Libro(isbn, nomeLibro, autoreLibro, prezzo, fullNameOwner);
        gestoreDB.addBook(nuovoLibro);
    } catch (Exception e) {
        out.println("Errore nell'aggiunta del libro: " + e.getMessage());
    }
}
