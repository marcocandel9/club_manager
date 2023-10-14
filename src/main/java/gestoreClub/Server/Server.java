package gestoreClub.Server;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;

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
