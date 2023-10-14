package gestoreClub.Data;
import org.bson.types.ObjectId;

import java.time.LocalDate;

public class Membro extends Persona{

    private LocalDate dataInizioIscrizione;
    private LocalDate dataFineIscrizione;



    // Il costruttore di Membro invoca il costruttore della classe padre, Persona, e inizializza le date di iscrizione.

    public Membro(String fiscalCodeId, String nome, String cognome, int eta, String email, String phoneNum) {
        super(fiscalCodeId, nome, cognome, eta,email,phoneNum);
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
