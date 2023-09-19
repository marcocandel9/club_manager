package gestoreClub.Data;

import java.util.ArrayList;

public class GestoreMembri implements MembroInterface {
    private final ArrayList<Membro> gestoreMembri;
    public GestoreMembri() {
        this.gestoreMembri = new ArrayList<>();
    }
    public ArrayList<Membro> getElencoMembri() {
        return gestoreMembri;
    }

    @Override
    public void aggiungiMembro(Membro membro) {
        this.gestoreMembri.add(membro);
        System.out.println(membro.getNome() + " è stato aggiunto dall'elenco membri."); // Usato un metodo getter
    }

    @Override
    public void rimuoviMembro(Membro membro) {
        this.gestoreMembri.remove(membro);
        System.out.println(membro.getNome() + " è stato rimosso dall'elenco membri."); // Usato un metodo getter
    }

    @Override
    public void rinnovaMembro(Membro membro) {
        membro.setDataInizioIscrizione(membro.getDataInizioIscrizione().plusYears(1)); // Usato metodi getter e setter
        membro.setDataFineIscrizione(membro.getDataFineIscrizione().plusYears(1)); // Usato metodi getter e setter
        System.out.println(membro.getNome() + " ha rinnovato l'abbonamento fino al: " + membro.getDataFineIscrizione()); // Usato un metodo getter
    }
}
