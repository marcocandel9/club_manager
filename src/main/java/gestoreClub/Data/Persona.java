package gestoreClub.Data;
import org.bson.types.ObjectId;
import java.sql.SQLOutput;

public abstract class Persona{
    protected ObjectId id;
    protected String nome;
    protected String cognome;
    protected int eta;
    protected String email;
    protected String phoneNum;

    //costruttore che crea un nuovo objectId per inserimento nel DB
    public Persona(String nome, String cognome, int eta) {
        this.id=new ObjectId();
        this.nome = nome;
        this.cognome = cognome;
        this.eta = eta;

    }
    //costruttore che prende come parametro un id per caricamente da DB
    public Persona(ObjectId _id,String nome, String cognome, int eta,String email, String phoneNum) {
        this.id=_id;
        this.nome = nome;
        this.cognome = cognome;
        this.eta = eta;
        this.email = email;
        this.phoneNum = phoneNum;

    }

    public Persona( String nome, String cognome, int eta, String email, String phoneNum) {
        this.id=new ObjectId();
        this.nome = nome;
        this.cognome = cognome;
        this.eta = eta;
        this.email = email;
        this.phoneNum = phoneNum;

    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public int getEta() {
        return eta;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
        System.out.println("Il numero " + phoneNum + " è stato assegnato a " + this.nome);
    }

    public void setEmail(String email) {
        this.email = email;
        System.out.println("l'email " + email + " è stata assegnata a " + this.nome);
    }

}

