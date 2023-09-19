package gestoreClub.Server;
import java.io.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.*;
import java.io.*;

public class ParserXML {
    private DocumentBuilderFactory databaseF;
    private DocumentBuilder db;
    private Document document;
    private Element root=null;
    private File file;
//costruttore che instanzia un nuovo DocumentBuilder e prende come parametro il nome del file di cui fare il parsing
    public ParserXML(File file){
        this.databaseF = DocumentBuilderFactory.newInstance();
        this.file = file;
    }
//elemento che fa il parse del file XML config del database e restituisce il root element.
    public Element ElementRootCatch(){

        try {
            db = this.databaseF.newDocumentBuilder();
            document=db.parse(file);
            root = document.getDocumentElement();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
        return root;

    }
}
