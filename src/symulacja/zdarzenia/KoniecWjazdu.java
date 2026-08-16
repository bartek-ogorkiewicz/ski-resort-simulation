package symulacja.zdarzenia;

import osrodek.Wyciag;
import sportowcy.Sportowiec;
import symulacja.Czas;
import symulacja.Planer;

/**
 * Zdarzenie oznaczające zakończenie wjazdu grupy pasażerów wyciągiem.
 * Po obsłużeniu każdy pasażer trafia do górnej stacji wyciągu
 * i podejmuje kolejną decyzję.
 */
public class KoniecWjazdu extends Zdarzenie {
    private final Sportowiec[] pasażerowie;
    private final int liczbaPasażerów;
    private final Wyciag wyciąg;

    public KoniecWjazdu(Czas czas, Sportowiec[] pasażerowie, int liczbaPasażerów, Wyciag wyciąg) {
        super(czas);

        assert pasażerowie != null;
        assert liczbaPasażerów > 0;
        assert liczbaPasażerów <= pasażerowie.length;
        assert wyciąg != null;

        for (int i = 0; i < liczbaPasażerów; i++) {
            assert pasażerowie[i] != null;
        }

        this.pasażerowie = pasażerowie;
        this.liczbaPasażerów = liczbaPasażerów;
        this.wyciąg = wyciąg;
    }

    @Override
    public void obsłuż(Planer planer) {
        assert planer != null;

        for (int i = 0; i < liczbaPasażerów; i++) {
            pasażerowie[i].zakończWjazd(wyciąg, planer);
        }
    }
}