package gestoreClub.Client;
import gestoreClub.Server.ParserXML;
import org.w3c.dom.Element;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

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
            else //devo stampare la domanda
                System.out.println(domandaServer);
        }
        //chiusura comunicazione
        in.close();
        out.close();
        socket.close();
    }

}

