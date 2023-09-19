package gestoreClub.Server;

import gestoreClub.Data.Membro;

import java.io.*;
import java.net.Socket;
import java.nio.Buffer;
import java.sql.SQLOutput;
import java.util.Scanner;

public class GestoreClient implements Runnable {

    private static final String CLOSE = "!"; //carattere che indica al client di voler chiudere la connessione
    private static final String ASK = "?"; //caratattere che indica al client di volere una risposta

    Socket socket;
    PrintWriter out;
    BufferedReader in;
    GestoreDB gestoreDB;

    public GestoreClient(Socket socket, GestoreDB gestoreDB) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(),true);
        this.gestoreDB = gestoreDB;
    }

    public void clientStart() throws Exception {
        Scanner keyboard = new Scanner(System.in);
        int rispostaClient, domandaServer;
        boolean esci = false; //flag esci che viene impostata nello switch case a true quando il client vuole terminare la comunciazione
        out.println("Benvenuto!");

        //qui verrà implementata logica di login
        while (!esci) {
            out.println("A quale funzione vuoi accedere?" + "\n1) Aggiungi membro" + "\n2) Rimuovi membro" + "\n3) Aggiunngi libro" + "\n4)Noleggio libro" + "\n5)Esci");
            out.println(ASK); //il server vuole una risposta
            rispostaClient = Integer.parseInt(in.readLine());
            switch (rispostaClient) {
                case 1 -> addMember();
                case 2 -> removeMember();
                case 5 -> esci = true;
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







    public void addMember() throws Exception {
        //da aggiungere logica di controllo del database nel caso in cui il membro sia già inserito chiedere di inserirne un altro comunque.

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
        Membro nuovoMembro = new Membro(nome, cognome, eta, email, phoneNum); //creazione membro
        gestoreDB.addMember(nuovoMembro); //conversione oggetto membro in oggetto documento e inserimento nella collezione membri
    }

    public void removeMember() throws Exception {
        //da implementare ricerca per objectId se necessaria
        out.println("Inserisci nome e cognome del membro da rimuovere:");
        out.println(ASK);
        String fullName = in.readLine();
        out.println("Inserisci eta");
        out.println(ASK);
        int eta = Integer.parseInt(in.readLine());
        String nome = fullName.split(" (?!.* )")[0];
        String cognome = fullName.split(" (?!.* )")[1];
        gestoreDB.removeMember(nome,cognome,eta); //rimozione del membro con query attraverso nome cognome età dalla collezione membri
    }

    public void rentBook() throws Exception {
        out.println("Inserisci il nome del libro da noleggiare:");
        out.println(ASK);
        String nomeLibro = in.readLine();
        out.println("Inserisci il nome e cognome del membro che vuole noleggiare il libro");
        out.println(ASK);
        String fullName = in.readLine();
        String nome = fullName.split(" (?!.* )")[0];
        String cognome = fullName.split(" (?!.* )")[1];
    }

    public void run() {
        try {
                clientStart();

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
}
