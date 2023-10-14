package gestoreClub.Server;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;
import gestoreClub.Data.Libro;
import gestoreClub.Data.Membro;
import org.bson.Document;
import org.bson.json.JsonObject;
import javax.print.Doc;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
public class GestoreDB {
        private static String uri;
        private static String dbname;
        private static MongoClient mongoClient;
        private static MongoDatabase database;
        private static MongoCollection<Document> tabMembri;
        private static MongoCollection<Document> tabLibri;

        public GestoreDB(String uri, String dbname) {
                mongoClient = MongoClients.create(uri);
                database = mongoClient.getDatabase(dbname);
                tabMembri = database.getCollection("Membri");
                tabLibri = database.getCollection("Libri");
        }

        public void addMember(Membro member) throws Exception {
                try {
                        Document newMember = new Document()
                                .append("_id", member.getfiscalCodeId())
                                .append("nome", member.getNome())
                                .append("cognome", member.getCognome())
                                .append("eta", member.getEta())
                                .append("email", member.getEmail())
                                .append("phoneNum", member.getPhoneNum())
                                .append("dataFineIscrizione", member.getDataFineIscrizione());
                        tabMembri.insertOne(newMember);
                        System.out.println("Membro aggiunto alla collezione Membri del db");
                } catch (Exception e) {
                        throw new Exception("Errore durante l'aggiunta del membro: " + e.getMessage());
                }
        }

        public void removeMember(String nome, String cognome, int eta) throws Exception {
                try {
                        Document query = new Document();
                        query.append("nome", nome);
                        query.append("cognome", cognome);
                        query.append("eta", eta);
                        Document deletedDocument = tabMembri.findOneAndDelete(query); //findOneAndDelete restituisce null se non trova nessun doc., restituisce il documento stesso se lo trova e lo elimina dal DB

                        //blocco condizionale che controlla se il documento sia stato trovato ed eliminato o meno.
                        if (deletedDocument != null) {
                                System.out.println("Documento eliminato: " + deletedDocument.toJson());
                        } else {
                                System.out.println("Documento non trovato.");
                        }
                } catch (Exception e) {
                        System.err.println("Errore durante l'eliminazione del membro" + e.getMessage());
                }
        }

        public void addBook(Libro libro) throws Exception {
                try {
                        Document newBook = new Document()
                                .append("_id", libro.getIsbn())
                                .append("isRented", libro.getRented())
                                .append("titolo", libro.getTitolo())
                                .append("autore", libro.getAutore())
                                .append("prezzoLibro", libro.getPrezzo())
                                .append("proprietarioLibro", libro.getProprietario())
                                .append("noleggio", new Document("caparra", null)  //con questa query viene inserito l'oggetto noleggio ( per ora con campi vuoti)
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

        public void rentBook(String isbn, String renterName, String fiscalCodeId, int caparra) throws Exception {
                try {

                        //blocco di query nel database per il libro da noleggiare
                        Document queryBook = new Document("_id", isbn); //documento utile a effettuare la query
                        Document fetchedBook = tabLibri.find(queryBook).first(); //metodo find(query) per trovare e restituire il documento
                        if (fetchedBook != null) {    //blocco condizionale che controlla che il libro sia presente nel database
                                System.out.println("Libro trovato." + fetchedBook.toJson());
                        } else {
                                System.out.println("Libro non trovato, riprovare.");
                                throw new Exception("Libro non presente nel database.");
                        }

                        //blocco di query nel database per il membro che noleggia il libro
                        Document queryMember = new Document("_id", fiscalCodeId); //documento utile a effettuare la query
                        Document fetchedMember = tabMembri.find(queryMember).first(); //metodo find(query) per trovare e restituire il documento
                        if (fetchedMember != null) {    //blocco condizionale che controlla che il membro sia presente nel database
                                System.out.println("Membro trovato." + fetchedMember.toJson());
                        } else {
                                System.out.println("Membro non trovato, riprovare.");
                                throw new Exception("Membro non presente nel database.");
                        }

                        //blocco di update del documento trovato nel database.
                        Document nestedFieldsToUpdate = new Document() //documento di aggiornamento con i dati
                                .append("isRented", true)
                                .append("noleggio", new Document("caparra", caparra)
                                        .append("renterName", renterName)
                                        .append("renterFiscalCodeId", fiscalCodeId)
                                        .append("rentStartDate", LocalDate.now()));

                        Document updateOperator = new Document("$set", nestedFieldsToUpdate); //l'operatore set prepara il documento di aggiornamento
                        tabLibri.findOneAndUpdate(fetchedBook, updateOperator); //ricerca del documento tramite fetchedBook e aggiornamento tramite l'update operator


                        System.out.println("Il libro è stato correttamente noleggiato.");

                } catch (Exception e) {
                        throw new Exception("Errore durante la registrazione dati noleggio nel db: " + e.getMessage(), e); //exception chaining
                }
        }

        public void unrentBook(String isbn) throws Exception {
                try {
                        //blocco di query nel database per il libro richiesto
                        Document queryBook = new Document("_id", isbn); //documento utile a effettuare la query
                        Document fetchedBook = tabLibri.find(queryBook).first(); //metodo find(query) per trovare e restituire il documento
                        if (fetchedBook != null) {    //blocco condizionale che controlla che il libro sia presente nel database
                                System.out.println("Libro trovato." + fetchedBook.toJson());
                        } else {
                                System.out.println("Libro non trovato, riprovare.");
                                throw new Exception("Libro non presente nel database.");
                        }

                        //blocco di update del documento trovato nel database. E' stata utilizzata la stessa logica del metodo rentBook, inserendo però i campi null.
                        Document nestedFieldsToUpdate = new Document() //documento di aggiornamento con i dati
                                .append("noleggio", new Document("caparra", null)
                                        .append("renterName", null)
                                        .append("renterFiscalCodeId", null)
                                        .append("rentStartDate", null));

                        Document updateOperator = new Document("$set", nestedFieldsToUpdate); //l'operatore set prepara il documento di aggiornamento
                        tabLibri.findOneAndUpdate(fetchedBook, updateOperator); //ricerca del documento tramite fetchedBook e aggiornamento tramite l'update operator

                        System.out.println("Il noleggio del libro è stato correttamente terminato.");


                } catch (Exception e) {
                        throw new Exception("Errore durante la cancellazione del noleggio: " + e.getMessage(), e); //exception chaining
                }
        }

        public ArrayList<String> showMembers() throws Exception {

                ArrayList<String> listaMembri = new ArrayList<>();
                try {

                        //queste due righe di codice utilizzano il metodo find() che itera e restituisce tutti i doocumenti, il metodo .projection che permette
                        //di specificare quali documenti debbano essere restituiti e vengono specificati i parametri da restituire per ogni documento grazie al parametro
                        //Projections.fields( ... )
                        //al metodo find può essere concatenato il metodo .forEach che itera su ogni documento restituito dalla query e stampa ciascun documento come stringa JSON.
                        tabMembri.find().projection(Projections.fields(Projections.include("nome", "cognome", "email", "dataFineIscrizione")))
                                .forEach(document -> listaMembri.add(document.toJson()));

                } catch (Exception e) {
                        System.out.println("Errore durante la visualizzazione della lista membri: " + e.getMessage());
                }
                return listaMembri;
        }

        public ArrayList<String> showBooks() throws Exception {

                ArrayList<String> listaLibri = new ArrayList<>();
                try {

                        //queste due righe di codice utilizzano il metodo find() che itera e restituisce tutti i doocumenti, il metodo .projection che permette
                        //di specificare quali documenti debbano essere restituiti e vengono specificati i parametri da restituire per ogni documento grazie al parametro
                        //Projections.fields( ... )
                        //al metodo find può essere concatenato il metodo .forEach che itera su ogni documento restituito dalla query e stampa ciascun documento come stringa JSON.
                        tabLibri.find().projection(Projections.fields(Projections.include("isRented", "titolo", "proprietarioLibro", "noleggio.renterName", "noleggio.renterFiscalCodeId", "noleggio.rentStartDate")))
                                .forEach(document -> listaLibri.add(document.toJson()));

                } catch (Exception e) {
                        System.out.println("Errore durante la visualizzazione della lista libri: " + e.getMessage());
                }
                return listaLibri;
        }
}






