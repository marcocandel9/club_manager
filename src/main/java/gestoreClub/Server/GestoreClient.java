package gestoreClub.Server;

import gestoreClub.Data.Libro;
import gestoreClub.Data.LibroNoleggiato;
import gestoreClub.Data.Membro;
import org.bson.types.ObjectId;

import java.io.*;
import java.net.Socket;
import java.nio.Buffer;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Scanner;

public class GestoreClient implements Runnable {

    private static final String CLOSE = "!"; //carattere che indica al client di voler chiudere la connessione
    private static final String ASK = "?"; //caratattere che indica al client di volere una risposta
    private ArrayList<String> listaLibri = new ArrayList<>();

    private ArrayList<String> listaMembri = new ArrayList<>();
    Socket socket;
    PrintWriter out;
    BufferedReader in;
    GestoreDB gestoreDB;

    public GestoreClient(Socket socket, GestoreDB gestoreDB) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        this.gestoreDB = gestoreDB;
    }

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


    //metodi che implementano le funzionalità del gestore client
    public void addMember() throws Exception {
        //da aggiungere logica di controllo del database nel caso in cui il membro sia già inserito chiedere di inserirne un altro comunque.

        try {
            out.println("Inserisci codice fiscale membro");
            out.println(ASK);
            String fiscalCodeId = in.readLine();
            out.println("Inserisci nome e cognome del membro:");
            out.println(ASK);
            String fullName = in.readLine();
            out.println("Inserisci eta");
            out.println(ASK);
            int eta = Integer.parseInt(in.readLine());
            out.println("Inserisci email");
            out.println(ASK);
            String email = in.readLine();
            out.println("Inserisci phoneNum");
            out.println(ASK);
            String phoneNum = in.readLine();
            String nome = fullName.split(" (?!.* )")[0];
            String cognome = fullName.split(" (?!.* )")[1];
            Membro nuovoMembro = new Membro(fiscalCodeId, nome, cognome, eta, email, phoneNum); //creazione membro

            gestoreDB.addMember(nuovoMembro); //conversione oggetto membro in oggetto documento e inserimento nella collezione membri
        } catch (NumberFormatException e) {
            out.println("Caratteri non validi, riprovare. :" + e);
        } catch (Exception ex) {
        }
    }

    public void removeMember() throws Exception {
        //da implementare ricerca per objectId se necessaria
        try {
            out.println("Inserisci nome e cognome del membro da rimuovere:");
            out.println(ASK);
            String fullName = in.readLine();
            out.println("Inserisci eta");
            out.println(ASK);
            int eta = Integer.parseInt(in.readLine());
            String nome = fullName.split(" (?!.* )")[0];
            String cognome = fullName.split(" (?!.* )")[1];
            gestoreDB.removeMember(nome, cognome, eta); //rimozione del membro con query attraverso nome cognome età dalla collezione membri
        } catch (NumberFormatException e) {
            out.println("Eta' non valida. Inserire un numero, riprovare. :" + e);
        } catch (Exception ex) {
        }
    }

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

    public void removeBook() throws Exception {
        try {
            out.println("Inserisci codice ISBN del libro da rimuovere:"); //valore identificativo unico di ogni libro
            String isbn = in.readLine();
            gestoreDB.removeBook(isbn); //chiamata al metodo removeBook del gestore DB

        } catch (Exception e) {
            out.println("Si è verificato un errore nella rimozione del libro. Riprovare. " + e.getMessage());
        }
    }

    public void rentBook() throws Exception {
        try {
            out.println("Inserisci il codice ISBN Identificativo del libro");
            out.println(ASK);
            String isbn = in.readLine();

            out.println("Inserisci nome e cognome del noleggiante");
            out.println(ASK);
            String renterName = in.readLine();

            out.println("Inserisci il codice fiscale del noleggiante");
            out.println(ASK);
            String fiscalCodeId = in.readLine();

            out.println("Inserisci la caparra per il noleggio del libro");
            out.println(ASK);
            int caparra = Integer.parseInt(in.readLine());
            //qui a differenza di addBook il libro noleggiato non viene creato in quanto viene cercato direttamente sul database.

            gestoreDB.rentBook(isbn, renterName, fiscalCodeId, caparra);
        } catch (Exception e) {
            out.println("Caratteri non validi. Riprovare. " + e.getMessage());
        }
    }

    public void unrentBook() throws Exception {
        try {
            out.println("Inserisci il codice ISBN Identificativo del libro");
            out.println(ASK);
            String isbn = in.readLine();
            gestoreDB.unrentBook(isbn);
            out.println("noleggio del libro: " + isbn + ", correttamente terminato.");
        } catch (Exception e) {
            out.println("Si è verificato un'errore col termine del noleggio. Riprovare. " + e.getMessage());
        }
    }

    public void showMembers() throws Exception {
        try {
            listaMembri = gestoreDB.showMembers(); //il metodo showMembers del gestoreDB ritorna un arrayList con i membri e viene assegnato a listaMembri
            for (String member : listaMembri) { //i membri vengono printati.
                out.println(member);
            }
        } catch (Exception e) {
        }
    }

    public void showBooks() throws Exception {
        try {
            listaLibri = gestoreDB.showBooks(); //il metodo showBooks del gestoreDB ritorna un arrayList con i libri e viene assegnato a listaLibri
            for (String libro : listaLibri) { //i libri vengono printati.
                out.println(libro);
            }
        } catch (Exception e) {

        }
    }
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
}


