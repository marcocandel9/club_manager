package gestoreClub.Data;
import org.bson.types.ObjectId;

import java.time.LocalDate;

public class Membro extends Persona{

    LocalDate dataInizioIscrizione;
    LocalDate dataFineIscrizione;

    //il costruttore Membro utilizza il costruttore della classe padre Persona, l'object ID per caricamento su DB è istanziato.

    public Membro(String nome, String cognome, int eta) {
        super(nome, cognome, eta);
        this.dataInizioIscrizione = LocalDate.now();
        this.dataFineIscrizione = this.dataInizioIscrizione.plusYears(1);

    }

    //Costruttore membro che richiama il costruttore padre persona con parametro ObjectID per caricamento da DB.

    public Membro(ObjectId id, String nome, String cognome, int eta, String email, String phoneNum) {
        super(id, nome, cognome, eta,email,phoneNum);
        this.dataInizioIscrizione = LocalDate.now();
        this.dataFineIscrizione = this.dataInizioIscrizione.plusYears(1);

    }

    //Overload della classe membro rispetto quella Padre
    public Membro(String nome, String cognome,int eta, String email, String phoneNum) {
        super( nome, cognome, eta, email, phoneNum);
        this.dataInizioIscrizione = LocalDate.now();
        this.dataFineIscrizione = this.dataInizioIscrizione.plusYears(1);

    }

    public LocalDate getDataInizioIscrizione() {
        return dataInizioIscrizione;
    }

    public void setDataInizioIscrizione(LocalDate dataInizioIscrizione) {
        this.dataInizioIscrizione = dataInizioIscrizione;
    }

    public LocalDate getDataFineIscrizione() {
        return dataFineIscrizione;
    }

    public void setDataFineIscrizione(LocalDate dataFineIscrizione) {
        this.dataFineIscrizione = dataFineIscrizione;
    }
}
