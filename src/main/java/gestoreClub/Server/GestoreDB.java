package gestoreClub.Server;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import gestoreClub.Data.Libro;
import gestoreClub.Data.Membro;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicReferenceArray;

import static com.mongodb.client.MongoClient.*;
import static java.lang.System.out;

public class GestoreDB {
        private static String uri;
        private static String dbname;
        private static MongoClient mongoClient;
        private static MongoDatabase database;
        private static MongoCollection<Document> TabMembri;
        private static MongoCollection<Document> Tablibri;
        private static ArrayList<Membro> listaMembri;
        private static ArrayList<Libro> listaLibri;

        public GestoreDB(String uri, String dbname) {
                mongoClient = MongoClients.create(uri);
                database = mongoClient.getDatabase(dbname);
                TabMembri = database.getCollection("Membri");
                Tablibri = database.getCollection("libri");
                listaMembri = new ArrayList<Membro>();
                listaLibri = new ArrayList<Libro>();
        }

        public void addMember(Membro member) throws Exception {
                try {
                        Document newMember = new Document();
                        newMember.append("_id", member.getId());
                        newMember.append("nome", member.getNome());
                        newMember.append("cognome", member.getCognome());
                        newMember.append("eta", member.getEta());
                        newMember.append("email", member.getEmail());
                        newMember.append("phoneNum", member.getPhoneNum());
                        newMember.append("dataFineIscrizione", member.getDataFineIscrizione());
                        TabMembri.insertOne(newMember);
                        System.out.println("Membro aggiunto al db");
                } catch (Exception e) {
                        throw new Exception("Errore durante l'aggiunta del membro: " + e.getMessage());
                }
        }

        public void removeMember(String nome, String cognome, int eta) throws Exception{
                try {
                        Document query = new Document();
                        query.append("nome", nome);
                        query.append("cognome", cognome);
                        query.append("eta", eta);
                        Document deletedDocument = TabMembri.findOneAndDelete(query); //findOneAndDelete restituisce null se non trova nessun doc., restituisce il documento stesso se lo trova e lo elimina dal DB

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


}
