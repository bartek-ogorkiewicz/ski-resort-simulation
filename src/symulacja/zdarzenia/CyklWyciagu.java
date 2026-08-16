package symulacja.zdarzenia;

import osrodek.Wyciag;
import symulacja.Czas;
import symulacja.Planer;

/**
 * Zdarzenie oznaczające kolejny cykl pracy wyciągu.
 * Po obsłużeniu wyciąg zabiera z kolejki kolejną grupę pasażerów
 * i planuje następny cykl.
 */
public class CyklWyciagu extends Zdarzenie {
    private final Wyciag wyciąg;

    public CyklWyciagu(Czas czas, Wyciag wyciąg) {
        super(czas);

        assert wyciąg != null;

        this.wyciąg = wyciąg;
    }

    @Override
    public void obsłuż(Planer planer) {
        assert planer != null;

        wyciąg.obsłużCykl(planer);
    }
}