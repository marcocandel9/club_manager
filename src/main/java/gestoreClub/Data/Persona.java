package gestoreClub.Data;
import org.bson.types.ObjectId;
import java.sql.SQLOutput;

public abstract class Persona{
    private String fiscalCodeId;
    private String nome;
    private String cognome;
    private int eta;
    private String email;
    private String phoneNum;

    //costruttore classe padre
    public Persona(String fiscalCodeId,String nome, String cognome, int eta,String email, String phoneNum) {
        this.fiscalCodeId=fiscalCodeId;
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
    public String getfiscalCodeId() {
        return fiscalCodeId;
    }
    public void setId(String fiscalCodeId) {
        this.fiscalCodeId = fiscalCodeId;
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

