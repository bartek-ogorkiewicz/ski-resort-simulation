package symulacja.zdarzenia;

import sportowcy.Sportowiec;
import symulacja.Czas;
import symulacja.Planer;

/**
 * Zdarzenie oznaczające przybycie sportowca na stok.
 * Po obsłużeniu sportowiec pojawia się w swoim węźle startowym
 * i podejmuje pierwszą decyzję.
 */
public class PrzybycieSportowca extends Zdarzenie {
    private final Sportowiec sportowiec;

    public PrzybycieSportowca(Czas czas, Sportowiec sportowiec) {
        super(czas);

        assert sportowiec != null;

        this.sportowiec = sportowiec;
    }

    @Override
    public void obsłuż(Planer planer) {
        assert planer != null;
        sportowiec.przyjedźNaStok(planer);
    }
}