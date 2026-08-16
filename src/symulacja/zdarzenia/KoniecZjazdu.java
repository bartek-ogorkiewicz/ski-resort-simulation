package symulacja.zdarzenia;

import osrodek.Trasa;
import sportowcy.Sportowiec;
import symulacja.Czas;
import symulacja.Planer;

/**
 * Zdarzenie oznaczające zakończenie zjazdu sportowca daną trasą.
 * Po obsłużeniu sportowiec trafia do końcowego węzła trasy
 * i podejmuje kolejną decyzję.
 */
public class KoniecZjazdu extends Zdarzenie {
    private final Sportowiec sportowiec;
    private final Trasa trasa;

    public KoniecZjazdu(Czas czas, Sportowiec sportowiec, Trasa trasa) {
        super(czas);
        assert sportowiec != null;
        assert trasa != null;

        this.sportowiec = sportowiec;
        this.trasa = trasa;
    }

    @Override
    public void obsłuż(Planer planer) {
        assert planer != null;
        sportowiec.zakończZjazd(trasa, planer);
    }
}
